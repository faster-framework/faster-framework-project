package cn.org.faster.framework.grpc.core.factory;

import cn.org.faster.framework.grpc.core.model.MethodCallProperty;
import io.grpc.MethodDescriptor;

/**
 * 序列化/反序列化解析器工厂
 *
 * @author zhangbowen
 * @since 2019/1/16
 */
public interface MarshallerFactory {

    MethodDescriptor.Marshaller<Object> emptyMarshaller();

    MethodDescriptor.Marshaller<Object> parseReturnMarshaller(MethodCallProperty methodCallProperty);

    MethodDescriptor.Marshaller<Object> parseRequestMarshaller(MethodCallProperty methodCallProperty);
}
