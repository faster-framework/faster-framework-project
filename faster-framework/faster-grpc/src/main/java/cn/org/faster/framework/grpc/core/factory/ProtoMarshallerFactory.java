package cn.org.faster.framework.grpc.core.factory;

import cn.org.faster.framework.grpc.core.marshaller.ProtoMarshaller;
import io.grpc.MethodDescriptor;

import java.lang.reflect.Type;

/**
 * @author zhangbowen
 * @since 2019/1/22
 */
public class ProtoMarshallerFactory extends AbstractMarshallerFactory {

    @Override
    protected MethodDescriptor.Marshaller<Object> generateMarshaller(Type type) {
        return new ProtoMarshaller(type);
    }
}
