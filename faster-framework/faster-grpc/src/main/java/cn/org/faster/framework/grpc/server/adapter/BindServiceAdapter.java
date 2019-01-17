package cn.org.faster.framework.grpc.server.adapter;

import cn.org.faster.framework.grpc.core.factory.FastJsonMarshallerFactory;
import cn.org.faster.framework.grpc.core.marshaller.FastJsonMarshaller;
import cn.org.faster.framework.grpc.core.model.MethodCallProperty;
import cn.org.faster.framework.grpc.server.exception.GrpcServerCreateException;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCallHandler;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/1/16
 */
@Data
public class BindServiceAdapter implements BindableService {
    private final String scheme;
    private final List<MethodCallProperty> methodCallList;

    public BindServiceAdapter(String scheme, List<MethodCallProperty> methodCallList) {
        this.scheme = scheme;
        this.methodCallList = methodCallList;
    }


    private MethodDescriptor<Object, Object> createMethodDescriptor(MethodCallProperty methodCallProperty) {
        return MethodDescriptor.newBuilder(
                FastJsonMarshallerFactory.parseRequestMarshaller(methodCallProperty),
                new FastJsonMarshaller()
        ).setType(methodCallProperty.getMethodType())
                .setFullMethodName(MethodDescriptor.generateFullMethodName(methodCallProperty.getScheme(), methodCallProperty.getMethodName()))
                .build();
    }

    @SuppressWarnings("unchecked")
    private ServerCallHandler<Object, Object> parseCallHandlerFromType(MethodCallProperty methodCallProperty) {
        Method method = methodCallProperty.getMethod();
        Object target = methodCallProperty.getProxyTarget();
        switch (methodCallProperty.getMethodType()) {
            case UNARY:
                return ServerCalls.asyncUnaryCall((request, responseObserver) -> {
                    try {
                        method.invoke(target, request, responseObserver);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                });
            case SERVER_STREAMING:
                return ServerCalls.asyncServerStreamingCall((request, responseObserver) -> {
                    try {
                        method.invoke(target, request, responseObserver);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
            case CLIENT_STREAMING:
                return ServerCalls.asyncClientStreamingCall(responseObserver -> {
                    try {
                        return (StreamObserver<Object>) method.invoke(target, responseObserver);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            case BIDI_STREAMING:
                return ServerCalls.asyncBidiStreamingCall(responseObserver -> {
                    try {
                        return (StreamObserver<Object>) method.invoke(target, responseObserver);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        }
        throw new GrpcServerCreateException("gRPC method type not match.Please check your class.");
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
