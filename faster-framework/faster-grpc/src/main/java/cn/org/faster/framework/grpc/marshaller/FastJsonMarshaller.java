package cn.org.faster.framework.grpc.marshaller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.grpc.MethodDescriptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * grpc传输装配器-jackson
 *
 * @author zhangbowen
 * @since 2019/1/13
 */
public class FastJsonMarshaller<T> implements MethodDescriptor.Marshaller<T> {

    @Override
    public InputStream stream(Object value) {
        return new ByteArrayInputStream(JSON.toJSONBytes(value));
    }

    @Override
    public T parse(InputStream stream) {
        try {
            return JSON.parseObject(stream, new TypeReference<T>() {
            }.getType());
        } catch (IOException ignored) {
        }
        return null;
    }
}
