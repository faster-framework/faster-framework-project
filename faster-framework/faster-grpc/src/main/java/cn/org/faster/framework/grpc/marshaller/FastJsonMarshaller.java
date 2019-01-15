package cn.org.faster.framework.grpc.marshaller;

import com.alibaba.fastjson.JSON;
import io.grpc.MethodDescriptor;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * grpc传输装配器-jackson
 *
 * @author zhangbowen
 * @since 2019/1/13
 */
@Data
public class FastJsonMarshaller implements MethodDescriptor.Marshaller<Object> {
    private final Type type;

    public FastJsonMarshaller(Type type) {
        this.type = type;
    }

    public FastJsonMarshaller() {
        this.type = null;
    }

    @Override
    public InputStream stream(Object value) {
        return new ByteArrayInputStream(JSON.toJSONBytes(value));
    }

    @Override
    public Object parse(InputStream stream) {
        try {
            return JSON.parseObject(stream, type);
        } catch (IOException ignored) {
        }
        return null;
    }
}
