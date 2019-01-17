package cn.org.faster.framework.grpc.core.marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
public class JacksonMarshaller implements MethodDescriptor.Marshaller<Object> {
    private final Type type;
    private final ObjectMapper objectMapper;

    public JacksonMarshaller(Type type, ObjectMapper objectMapper) {
        this.type = type;
        this.objectMapper = objectMapper;
    }

    public JacksonMarshaller(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.type = null;
    }

    @Override
    public InputStream stream(Object value) {
        if (value == null) {
            return new ByteArrayInputStream(new byte[]{});
        }
        try {
            return new ByteArrayInputStream(objectMapper.writeValueAsBytes(value));
        } catch (JsonProcessingException ignored) {
        }
        return new ByteArrayInputStream(new byte[]{});
    }

    @Override
    public Object parse(InputStream stream) {
        if (stream == null || type == null || type.getTypeName().equals("void")) {
            return new Object();
        }
        try {
            return objectMapper.readValue(stream, TypeFactory.defaultInstance().constructType(type));
        } catch (IOException ignored) {
        }
        return new Object();
    }
}
