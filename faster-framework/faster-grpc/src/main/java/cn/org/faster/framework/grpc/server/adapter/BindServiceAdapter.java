package cn.org.faster.framework.grpc.server.adapter;

import cn.org.faster.framework.grpc.core.factory.FastJsonMarshallerFactory;
import cn.org.faster.framework.grpc.core.marshaller.FastJsonMarshaller;
import cn.org.faster.framework.grpc.core.model.MethodCallProperty;
import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCallHandler;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/1/16
 */
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

    private ServerCallHandler<Object, Object> parseCallHandlerFromType(MethodCallProperty methodCallProperty) {
        //todo
        return ServerCalls.asyncClientStreamingCall(new ServerCalls.ClientStreamingMethod<Object, Object>() {
            @Override
            public StreamObserver<Object> invoke(StreamObserver<Object> responseObserver) {
                return null;
            }
        });
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
