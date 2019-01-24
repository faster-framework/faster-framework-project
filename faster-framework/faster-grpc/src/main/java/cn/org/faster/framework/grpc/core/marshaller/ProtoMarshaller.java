package cn.org.faster.framework.grpc.core.marshaller;

import io.grpc.MethodDescriptor;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * grpc传输装配器-jackson
 *
 * @author zhangbowen
 * @since 2019/1/13
 */
@SuppressWarnings("unchecked")
public class ProtoMarshaller implements MethodDescriptor.Marshaller<Object> {
    private Schema<Object> responseSchema;
    private Schema<Object> requestSchema;
    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    public ProtoMarshaller(Type type) {
        if (type != null) {
            Class clazz;
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                clazz = ((Class) pt.getRawType());
            } else {
                clazz = (Class) type;
            }
            this.responseSchema = RuntimeSchema.getSchema(clazz);
        }
    }

    @Override
    public InputStream stream(Object value) {
        if (value == null) {
            return new ByteArrayInputStream(new byte[]{});
        }
        if (requestSchema == null) {
            requestSchema = RuntimeSchema.getSchema((Class<Object>) value.getClass());
        }
        return new ByteArrayInputStream(ProtobufIOUtil.toByteArray(value, requestSchema, buffer));
    }

    @Override
    public Object parse(InputStream stream) {
        if (stream == null) {
            return new Object();
        }
        Object obj = responseSchema.newMessage();
        try {
            ProtobufIOUtil.mergeFrom(StreamUtils.copyToByteArray(stream), obj, responseSchema);
            return obj;
        } catch (IOException ignored) {
        }
        return new Object();
    }
}
