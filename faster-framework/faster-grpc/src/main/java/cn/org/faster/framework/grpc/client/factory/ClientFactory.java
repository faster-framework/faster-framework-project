package cn.org.faster.framework.grpc.client.factory;

import cn.org.faster.framework.grpc.annotation.GrpcMethod;
import cn.org.faster.framework.grpc.client.annotation.GrpcService;
import cn.org.faster.framework.grpc.client.exception.ChannelCreateException;
import cn.org.faster.framework.grpc.client.model.ChannelProperty;
import cn.org.faster.framework.grpc.client.model.MethodCallProperty;
import cn.org.faster.framework.grpc.client.proxy.ManageChannelProxy;
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
    /**
     * 服务名-连接配置 字典
     */
    private Map<String, ChannelProperty> serverChannelMap = new HashMap<>();


    public Object createClientProxy(Class<?> target) {
        GrpcService grpcService = target.getAnnotation(GrpcService.class);
        ChannelProperty channelProperty = serverChannelMap.get(grpcService.value());
        if (channelProperty == null) {

            throw new ChannelCreateException("GrpcService scheme:{" + grpcService.value() + "} was not found in properties.Please check your configuration.");
        }
        ManageChannelProxy manageChannelProxy = new ManageChannelProxy(channelProperty);
        //获取该类下所有包含GrpcMethod的注解，创建call
        Map<Method, GrpcMethod> annotatedMethods = MethodIntrospector.selectMethods(target,
                (MethodIntrospector.MetadataLookup<GrpcMethod>) method -> AnnotatedElementUtils.findMergedAnnotation(method, GrpcMethod.class));
        annotatedMethods.forEach((k, v) -> {
            String annotationMethodName = v.value();
            MethodCallProperty methodCallProperty = new MethodCallProperty();
            methodCallProperty.setMethod(k);
            methodCallProperty.setMethodName(StringUtils.isEmpty(annotationMethodName) ? k.getName() : annotationMethodName);
            methodCallProperty.setMethodType(v.type());
            methodCallProperty.setScheme(grpcService.scheme());
            manageChannelProxy.addCall(methodCallProperty);
        });
        return Proxy.newProxyInstance(target.getClassLoader(), new Class[]{target}, manageChannelProxy);
    }
}
