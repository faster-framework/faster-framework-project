package cn.org.faster.framework.grpc.client.factory;

import cn.org.faster.framework.grpc.client.annotation.GRpcService;
import cn.org.faster.framework.grpc.client.exception.GRpcChannelCreateException;
import cn.org.faster.framework.grpc.client.model.ChannelProperty;
import cn.org.faster.framework.grpc.client.proxy.ManageChannelProxy;
import cn.org.faster.framework.grpc.core.annotation.GRpcMethod;
import cn.org.faster.framework.grpc.core.factory.MarshallerFactory;
import cn.org.faster.framework.grpc.core.model.MethodCallProperty;
import lombok.Data;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
@Data
public class ClientFactory {
    private final MarshallerFactory marshallerFactory;
    /**
     * 服务名-连接配置 字典，factory创建时设置
     */
    private Map<String, ChannelProperty> serverChannelMap = new HashMap<>();

    public ClientFactory(MarshallerFactory marshallerFactory) {
        this.marshallerFactory = marshallerFactory;
    }

    public Object createClientProxy(Class<?> target) {
        GRpcService grpcService = target.getAnnotation(GRpcService.class);
        ChannelProperty channelProperty = serverChannelMap.get(grpcService.value());
        if (channelProperty == null) {
            throw new GRpcChannelCreateException("GRpcService scheme:{" + grpcService.value() + "} was not found in properties.Please check your configuration.");
        }
        ManageChannelProxy manageChannelProxy = new ManageChannelProxy(channelProperty, marshallerFactory);
        return Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, manageChannelProxy);
    }
}
