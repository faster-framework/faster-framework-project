package cn.org.faster.framework.grpc.client.proxy;

import cn.org.faster.framework.grpc.client.model.ChannelProperty;
import cn.org.faster.framework.grpc.client.model.MethodCallProperty;
import cn.org.faster.framework.grpc.marshaller.FastJsonMarshaller;
import io.grpc.*;
import io.grpc.stub.ClientCalls;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbowen
 * @since 2019/1/14
 */
public class ManageChannelProxy implements InvocationHandler {
    private ManagedChannel channel;
    private Map<String, ClientCall<Object, Object>> clientCallMap = new HashMap<>();

    public ManageChannelProxy(ChannelProperty channelProperty) {
        this.channel = ManagedChannelBuilder.forAddress(channelProperty.getHost(), channelProperty.getPort())
                .usePlaintext()
                .build();
    }

    public void addCall(MethodCallProperty methodCallProperty) {
        MethodDescriptor.Builder<Object, Object> builder = MethodDescriptor.newBuilder(
                new FastJsonMarshaller(),
                new FastJsonMarshaller(methodCallProperty.getMethod().getGenericReturnType()))
                .setType(methodCallProperty.getMethodType())
                .setFullMethodName(MethodDescriptor.generateFullMethodName(methodCallProperty.getScheme(), methodCallProperty.getMethodName()));
        clientCallMap.put(methodCallProperty.getMethod().getName(), channel.newCall(builder.build(), CallOptions.DEFAULT));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        ClientCall<Object, Object> clientCall = clientCallMap.get(method.getName());
        return ClientCalls.blockingUnaryCall(clientCall, args[0]);
    }

}
