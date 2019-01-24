package cn.org.faster.framework.grpc.core.factory;

import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.grpc.core.exception.GRpcMethodNoMatchException;
import cn.org.faster.framework.grpc.core.model.MethodCallProperty;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.MethodDescriptor;
import io.grpc.stub.StreamObserver;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * @author zhangbowen
 * @since 2019/1/22
 */
public abstract class AbstractMarshallerFactory implements MarshallerFactory {

    @Override
    public MethodDescriptor.Marshaller<Object> emptyMarshaller() {
        return generateMarshaller(null);
    }

    /**
     * 获取返回类型的解析器
     *
     * @param methodCallProperty methodCallProperty
     * @return 解析器
     */
    @Override
    public MethodDescriptor.Marshaller<Object> parseReturnMarshaller(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Type[] types;
        boolean existParam;
        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                if (method.getReturnType() == ListenableFuture.class) {
                    //等于ClientCalls.futureUnaryCall()
                    //获取ListenableFuture的泛型
                    types = Utils.reflectMethodReturnTypes(method);
                    return generateMarshaller(Utils.safeElement(types, 0));
                } else if (method.getReturnType().getName().equals("void")) {
                    //判断参数中是否存在StreamObserver泛型
                    //参数中为StreamObserver的泛型
                    types = Utils.reflectMethodParameterTypes(method, StreamObserver.class);
                    //说明参数中不含有StreamObserver参数
                    if (types == null) {
                        //返回普通方式
                        return generateMarshaller(method.getGenericReturnType());
                    }
                    //存在，相当于ClientCalls.asyncUnaryCall();
                    //检查是否存在两个参数，一个为业务参数，一个为StreamObserver，并且顺序一致
                    checkTwoParamHasStreamObServer(methodCallProperty);
                    return generateMarshaller(Utils.safeElement(types, 0));
                }
                //直接返回方法的返回类型，等于ClientCalls.blockingUnaryCall
                return generateMarshaller(method.getGenericReturnType());
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
                return generateMarshaller(Utils.safeElement(types, 0));
            case CLIENT_STREAMING: //客户端流。等于ClientCalls.asyncClientStreamingCall()
                //检查是否存在一个参数，为StreamObserver
                checkOneParamHasStreamObServer(methodCallProperty);
                //方法返回类型不为空时，必须为StreamObserver，检验（此为客户端传输数据所用，服务端响应在参数的StreamObserver中）
                if (!method.getReturnType().getName().equals("void") && method.getReturnType() != StreamObserver.class) {
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
                return generateMarshaller(Utils.safeElement(types, 0));
            case SERVER_STREAMING://等于ClientCalls.blockingServerStreamingCall
                if (method.getReturnType() != Iterator.class) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [Iterator] for your return value.Please check it.");
                }
                //获取返回类型的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return generateMarshaller(Utils.safeElement(types, 0));
        }
        throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                "Return value type no match.Please check your configuration.");
    }

    /**
     * 获取请求类型的解析器
     *
     * @param methodCallProperty methodCallProperty
     * @return 解析器
     */
    @Override
    public MethodDescriptor.Marshaller<Object> parseRequestMarshaller(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Type[] types;
        switch (methodCallProperty.getMethodType()) {
            case UNARY: //一对一，等于asyncUnaryCall()
                //检验是否两个参数，包含StreamObserver，并且顺序一致
                if (method.getGenericParameterTypes().length == 2) {
                    checkTwoParamHasStreamObServer(methodCallProperty);
                }
                //获取获取请求参数类型，第一个为业务实体
                types = method.getGenericParameterTypes();
                return generateMarshaller(Utils.safeElement(types, 0));
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
                return generateMarshaller(Utils.safeElement(types, 0));
            case CLIENT_STREAMING: //客户端流。等于asyncClientStreamingCall()
                //检查方法的返回值是否为StreamObserver类型
                if (method.getReturnType() != StreamObserver.class) {
                    throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                            "You should use [StreamObserver] for your return value.Please check it.");
                }
                //检验返回流StreamObserver是否存在，并且唯一。
                checkOneParamHasStreamObServer(methodCallProperty);
                //获取返回类型的泛型，为请求参数的泛型
                types = Utils.reflectMethodReturnTypes(method);
                return generateMarshaller(Utils.safeElement(types, 0));
            case SERVER_STREAMING://等于asyncServerStreamingCall()
                //检验是否两个参数，并且顺序一致
                if (method.getGenericParameterTypes().length == 2) {
                    checkTwoParamHasStreamObServer(methodCallProperty);
                } else {
                    checkOneParamHasStreamObServer(methodCallProperty);
                }
                //获取获取请求参数类型，第一个为业务实体
                types = method.getGenericParameterTypes();
                return generateMarshaller(Utils.safeElement(types, 0));
        }
        throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodCallProperty.getMethodType().name(),
                "Request value type no match.Please check your configuration.");
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
        Type type = Utils.safeElement(types, 0);
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
        Type type = Utils.safeElement(types, 1);
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
     * 生成装配器
     *
     * @param type 泛型类型
     * @return 装配器
     */
    protected abstract MethodDescriptor.Marshaller<Object> generateMarshaller(Type type);
}
