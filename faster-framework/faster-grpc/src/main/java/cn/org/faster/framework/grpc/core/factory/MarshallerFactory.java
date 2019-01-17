package cn.org.faster.framework.grpc.core.factory;

import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.grpc.core.exception.GRpcMethodNoMatchException;
import cn.org.faster.framework.grpc.core.marshaller.JacksonMarshaller;
import cn.org.faster.framework.grpc.core.model.MethodCallProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * 序列化/反序列化解析器工厂
 *
 * @author zhangbowen
 * @since 2019/1/16
 */
public class MarshallerFactory {
    private final ObjectMapper objectMapper;

    public MarshallerFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JacksonMarshaller emptyJacksonMarshaller() {
        return new JacksonMarshaller(objectMapper);
    }

    /**
     * 获取返回类型的解析器
     *
     * @param methodCallProperty methodCallProperty
     * @return 解析器
     */
    public JacksonMarshaller parseReturnMarshaller(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Type[] types;
        boolean existParam;
        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                if (method.getReturnType() == ListenableFuture.class) {
                    //等于ClientCalls.futureUnaryCall()
                    //获取ListenableFuture的泛型
                    types = Utils.reflectMethodReturnTypes(method);
                    return new JacksonMarshaller(types[0], objectMapper);
                } else if (method.getReturnType().getName().equals("void")) {
                    //检查是否存在两个参数，一个为业务参数，一个为StreamObserver，并且顺序一致
                    checkTwoParamHasStreamObServer(methodCallProperty);
                    //等于ClientCalls.asyncUnaryCall();
                    //参数中为StreamObserver的泛型
                    types = Utils.reflectMethodParameterTypes(method, StreamObserver.class);
                    //说明参数中不含有StreamObserver参数
                    if (types == null) {
                        throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                                "You use void for your return type.And you should use [StreamObserver] for your param value.Please check it.");
                    }
                    return new JacksonMarshaller(types[0], objectMapper);
                }
                //直接返回方法的返回类型，等于ClientCalls.blockingUnaryCall
                return new JacksonMarshaller(method.getGenericReturnType(), objectMapper);
            case BIDI_STREAMING://双向流，相当于asyncBidiStreamingCall
                //检查是否存在一个参数，为StreamObserver
                checkOneParamHasStreamObServer(methodCallProperty);
                //判断方法返回类型是否为StreamObserver（此为客户端传输数据所用，服务端响应在参数的StreamObserver中）
                if (method.getReturnType() != StreamObserver.class) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your return value.Please check it.");
                }
                //检验参数是否为StreamObserver，获取服务端响应泛型
                existParam = Utils.checkMethodHasParamClass(method, StreamObserver.class);
                if (!existParam) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your param value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new JacksonMarshaller(types[0], objectMapper);
            case CLIENT_STREAMING: //客户端流。等于ClientCalls.asyncClientStreamingCall()
                //检查是否存在一个参数，为StreamObserver
                checkOneParamHasStreamObServer(methodCallProperty);
                //判断方法返回类型是否为StreamObserver（此为客户端传输数据所用，服务端响应在参数的StreamObserver中）
                if (method.getReturnType() != StreamObserver.class) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your return value.Please check it.");
                }
                //检验参数是否为StreamObserver，获取服务端响应泛型
                existParam = Utils.checkMethodHasParamClass(method, StreamObserver.class);
                if (!existParam) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your param value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new JacksonMarshaller(types[0], objectMapper);
            case SERVER_STREAMING://等于ClientCalls.blockingServerStreamingCall
                if (method.getReturnType() != Iterator.class) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [Iterator] for your return value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new JacksonMarshaller(types[0], objectMapper);
        }
        throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                "Return value type no match.Please check your configuration.");
    }

    /**
     * 检查方法是否包含一个参数，为StreamObserver
     *
     * @param methodCallProperty 方法
     */
    private void checkOneParamHasStreamObServer(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        //判断当前方法是否仅包含一个参数，为StreamObserver。如果不是，抛出异常。
        Type[] types = method.getGenericParameterTypes();
        if (types == null || types.length != 1) {
            throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                    "You should use one param [StreamObserver] in your method.Please check it.");
        }
        //检查第一个参数是否为StreamObserver
        Type type = types[0];
        if (type instanceof ParameterizedType) {
            if (!((ParameterizedType) type).getRawType().getTypeName().equals(StreamObserver.class.getName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                        "You should use one param [StreamObserver] in your method.Please check it.");
            }
        } else {
            if (!type.getTypeName().equals(StreamObserver.class.getName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                        "You should use one param [StreamObserver] in your method.Please check it.");
            }
        }
    }

    /**
     * 检查方法是否包含两个参数，一个为业务实体，另外一个为StreamObserver
     *
     * @param methodCallProperty 方法
     */
    private void checkTwoParamHasStreamObServer(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        //判断当前方法是否仅包含两个参数，一个为请求实体，一个为StreamObserver。如果不是，抛出异常。
        Type[] types = method.getGenericParameterTypes();
        if (types == null || types.length != 2) {
            throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                    "You should use two param in your method.One of your [Business Request Bean],another is [StreamObserver].And the order must be consistent.Please check it.");
        }
        //检查第二个参数是否为StreamObserver
        Type type = types[1];
        if (type instanceof ParameterizedType) {
            if (!((ParameterizedType) type).getRawType().getTypeName().equals(StreamObserver.class.getName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                        "You should use two param in your method.One of your [Business Request Bean],another is [StreamObserver].And the order must be consistent.Please check it.");
            }
        } else {
            if (!type.getTypeName().equals(StreamObserver.class.getName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                        "You should use two param in your method.One of your [Business Request Bean],another is [StreamObserver].And the order must be consistent.Please check it.");
            }
        }
    }

    /**
     * 获取请求类型的解析器
     *
     * @param methodCallProperty methodCallProperty
     * @return 解析器
     */
    public JacksonMarshaller parseRequestMarshaller(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Type[] types;
        switch (methodCallProperty.getMethodType()) {
            case UNARY: //一对一，等于asyncUnaryCall()
                //检验是否两个参数，包含StreamObserver，并且顺序一致
                checkTwoParamHasStreamObServer(methodCallProperty);
                //获取获取请求参数类型，第一个为业务实体
                types = method.getGenericParameterTypes();
                return new JacksonMarshaller(types[0], objectMapper);
            case BIDI_STREAMING://双向流，等于asyncBidiStreamingCall()
                //检验是否一个参数，为StreamObserver
                checkOneParamHasStreamObServer(methodCallProperty);
                //检查方法的返回值是否为StreamObserver类型
                if (method.getReturnType() != StreamObserver.class) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your return value.Please check it.");
                }
                //获取方法参数类型为StreamObserver的泛型
                types = Utils.reflectMethodParameterTypes(method, StreamObserver.class);
                if (types == null) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your param value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new JacksonMarshaller(types[0], objectMapper);
            case CLIENT_STREAMING: //客户端流。等于asyncClientStreamingCall()
                //检验是否一个参数，为StreamObserver
                checkOneParamHasStreamObServer(methodCallProperty);
                //检查方法的返回值是否为StreamObserver类型
                if (method.getReturnType() != StreamObserver.class) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your return value.Please check it.");
                }
                //获取方法参数类型为StreamObserver的泛型
                types = Utils.reflectMethodParameterTypes(method, StreamObserver.class);
                if (types == null) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your param value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return new JacksonMarshaller(types[0], objectMapper);
            case SERVER_STREAMING://等于asyncServerStreamingCall()
                //检验是否两个参数，并且顺序一致
                checkTwoParamHasStreamObServer(methodCallProperty);
                //获取获取请求参数类型，第一个为业务实体
                types = method.getGenericParameterTypes();
                return new JacksonMarshaller(types[0], objectMapper);
        }
        throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                "Request value type no match.Please check your configuration.");
    }

}
