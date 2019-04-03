package cn.org.faster.framework.grpc.server.adapter;

import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.grpc.core.exception.GRpcMethodNoMatchException;
import cn.org.faster.framework.grpc.core.factory.MarshallerFactory;
import cn.org.faster.framework.grpc.core.model.MethodCallProperty;
import cn.org.faster.framework.grpc.server.exception.GRpcServerCreateException;
import cn.org.faster.framework.grpc.server.exception.GRpcServerException;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCallHandler;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/1/16
 */
@Data
public class BindServiceAdapter implements BindableService {
    private final String scheme;
    private final List<MethodCallProperty> methodCallList;
    private final MarshallerFactory marshallerFactory;

    public BindServiceAdapter(String scheme, List<MethodCallProperty> methodCallList, MarshallerFactory marshallerFactory) {
        this.scheme = scheme;
        this.methodCallList = methodCallList;
        this.marshallerFactory = marshallerFactory;
    }


    private MethodDescriptor<Object, Object> createMethodDescriptor(MethodCallProperty methodCallProperty) {
        return MethodDescriptor.newBuilder(
                marshallerFactory.parseRequestMarshaller(methodCallProperty),
                marshallerFactory.emptyMarshaller()
        ).setType(methodCallProperty.getMethodType())
                .setFullMethodName(MethodDescriptor.generateFullMethodName(methodCallProperty.getScheme(), methodCallProperty.getMethodName()))
                .build();
    }

    /**
     * 检查方法是否包含两个参数，一个为业务实体，另外一个为StreamObserver
     *
     * @param method     方法
     * @param methodType methodType
     */
    private void checkTwoParamHasStreamObServer(Method method, MethodDescriptor.MethodType methodType) {
        //判断当前方法是否仅包含两个参数，一个为请求实体，一个为StreamObserver。如果不是，抛出异常。
        Type[] types = method.getGenericParameterTypes();
        if (types == null || types.length != 2) {
            throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodType.name(),
                    "You should use two param in your method.One of your [Business Request Bean],another is [StreamObserver].And the order must be consistent.Please check it.");
        }
        //检查第二个参数是否为StreamObserver
        Type type = Utils.safeElement(types, 1);
        if (type instanceof ParameterizedType) {
            if (!((ParameterizedType) type).getRawType().getTypeName().equals(StreamObserver.class.getName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodType.name(),
                        "You should use two param in your method.One of your [Business Request Bean],another is [StreamObserver].And the order must be consistent.Please check it.");
            }
        } else {
            if (!StreamObserver.class.getName().equals(type.getTypeName())) {
                throw new GRpcMethodNoMatchException(method.getDeclaringClass().getName(), method.getName(), methodType.name(),
                        "You should use two param in your method.One of your [Business Request Bean],another is [StreamObserver].And the order must be consistent.Please check it.");
            }
        }
    }


    private void invokeMethodWithParamSize(Object target, Method method, int paramSize, Object request, StreamObserver<Object> responseObserver, MethodDescriptor.MethodType methodType) throws InvocationTargetException, IllegalAccessException {
        if (paramSize == 2) {
            checkTwoParamHasStreamObServer(method, methodType);
            method.invoke(target, request, responseObserver);
        } else if (paramSize == 1) {
            //判断这个方法是StreamObserver还是业务实体
            if (Utils.checkMethodHasParamClass(method, StreamObserver.class)) {
                //传递StreamObserver
                method.invoke(target, responseObserver);
            } else {
                //业务实体，将业务传递，返回空
                method.invoke(target, request);
                responseObserver.onNext(null);
                responseObserver.onCompleted();
            }
        } else if (paramSize == 0) {
            method.invoke(target);
            responseObserver.onNext(null);
            responseObserver.onCompleted();
        }
    }

    /**
     *
     * @param throwable 多个异常
     * @return 统一返回异常信息
     */
    private String processExceptionMessage(Throwable throwable) {
        return "GRpc server error.";
    }

    @SuppressWarnings("unchecked")
    private ServerCallHandler<Object, Object> parseCallHandlerFromType(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Object target = methodCallProperty.getProxyTarget();
        int paramSize = method.getGenericParameterTypes().length;
        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                return ServerCalls.asyncUnaryCall((request, responseObserver) -> {
                    try {
                        invokeMethodWithParamSize(target, method, paramSize, request, responseObserver, MethodDescriptor.MethodType.UNARY);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        exception.printStackTrace();
                        throw new GRpcServerException(processExceptionMessage(exception));
                    }
                });
            case SERVER_STREAMING:
                return ServerCalls.asyncServerStreamingCall((request, responseObserver) -> {
                    try {
                        invokeMethodWithParamSize(target, method, paramSize, request, responseObserver, MethodDescriptor.MethodType.SERVER_STREAMING);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        exception.printStackTrace();
                        throw new GRpcServerException(processExceptionMessage(exception));
                    }
                });
            case CLIENT_STREAMING:
                return ServerCalls.asyncClientStreamingCall(responseObserver -> {
                    try {
                        return (StreamObserver<Object>) method.invoke(target, responseObserver);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        exception.printStackTrace();
                        throw new GRpcServerException(processExceptionMessage(exception));
                    }
                });
            case BIDI_STREAMING:
                return ServerCalls.asyncBidiStreamingCall(responseObserver -> {
                    try {
                        return (StreamObserver<Object>) method.invoke(target, responseObserver);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        exception.printStackTrace();
                        throw new GRpcServerException(processExceptionMessage(exception));
                    }
                });
        }
        throw new GRpcServerCreateException("GRpc method type not match.Please check your class.");
    }

    /**
     * 创建监听服务
     *
     * @return 监听服务定义
     */
    @Override
    public ServerServiceDefinition bindService() {
        ServerServiceDefinition.Builder builder = ServerServiceDefinition.builder(scheme);
        for (MethodCallProperty methodCallProperty : methodCallList) {
            //根据方法类型，获取不同的handler监听
            builder.addMethod(createMethodDescriptor(methodCallProperty), parseCallHandlerFromType(methodCallProperty));
        }
        return builder.build();
    }
}
