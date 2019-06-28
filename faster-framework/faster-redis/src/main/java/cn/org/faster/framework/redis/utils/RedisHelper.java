package cn.org.faster.framework.redis.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Type;

/**
 * @author zhangbowen
 * @since 2019/5/27
 */
public class RedisHelper {
    private static RedisConnectionFactory factory;
    private static ObjectMapper objectMapper;

    public RedisHelper(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        RedisHelper.factory = factory;
        RedisHelper.objectMapper = objectMapper;
    }

    /**
     * 创建泛型template
     *
     * @param clazz 泛型类
     * @param <T>   定义泛型
     * @return RedisTemplate
     */
    public static <T> RedisTemplate<String, T> template(Class<T> clazz) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(clazz);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 创建泛型template
     *
     * @param <T> 定义泛型
     * @return RedisTemplate
     */
    public static <T> RedisTemplate<String, T> template() {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 创建泛型template
     *
     * @param type 泛型
     * @param <T>  定义泛型
     * @return RedisTemplate
     */
    public static <T> RedisTemplate<String, T> template(Type type) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(TypeFactory.defaultInstance().constructType(type));
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
