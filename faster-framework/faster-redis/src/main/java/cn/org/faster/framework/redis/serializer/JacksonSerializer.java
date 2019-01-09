package cn.org.faster.framework.redis.serializer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author zhangbowen
 * @since 2019/1/9
 */
public class JacksonSerializer<T> implements RedisSerializer<T> {
    private final ObjectMapper objectMapper;
    private static final byte[] EMPTY_ARRAY = new byte[0];

    public JacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            try {
                SerializerBean serializerBean = this.objectMapper.readValue(bytes, 0, bytes.length, SerializerBean.class);
                return this.objectMapper.readValue(serializerBean.getJson(), serializerBean.getType());
            } catch (Exception var3) {
                throw new SerializationException("Could not read JSON: " + var3.getMessage(), var3);
            }
        }
    }

    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return EMPTY_ARRAY;
        } else {
            try {
                SerializerBean serializerBean = new SerializerBean();
                serializerBean.setJson(this.objectMapper.writeValueAsString(t));
                serializerBean.setType(getJavaType(t.getClass()));
                return this.objectMapper.writeValueAsBytes(serializerBean);
            } catch (Exception var3) {
                throw new SerializationException("Could not write JSON: " + var3.getMessage(), var3);
            }
        }
    }

    private JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructType(clazz);
    }

}
