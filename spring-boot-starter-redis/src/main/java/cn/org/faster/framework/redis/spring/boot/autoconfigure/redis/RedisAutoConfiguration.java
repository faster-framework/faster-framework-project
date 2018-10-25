package cn.org.faster.framework.redis.spring.boot.autoconfigure.redis;

import cn.org.faster.framework.redis.annotation.RedisListener;
import cn.org.faster.framework.redis.annotation.RedisListenerScan;
import cn.org.faster.framework.redis.registrar.SubscribeListenerScannerRegistrar;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;

/**
 * @author zhangbowen
 * @since 2018/10/10
 */
@Configuration
@RedisListenerScan(annotationClass = RedisListener.class)
@Import({SubscribeListenerScannerRegistrar.class, RedisCacheAutoConfiguration.class})
public class RedisAutoConfiguration {
    /**
     * 设置redisTemplate序列化
     *
     * @param redisConnectionFactory 连接工厂
     * @param objectMapper           json序列化
     * @return RedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory, ApplicationContext applicationContext) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RedisListener.class);
        beansWithAnnotation.forEach((k, v) -> {
            RedisListener redisListener = AnnotationUtils.findAnnotation(v.getClass(), RedisListener.class);
            if (redisListener == null) {
                return;
            }
            String[] channels = redisListener.value();
            if (channels.length == 0) {
                return;
            }
            if (v instanceof MessageListener) {
                for (String channel : channels) {
                    container.addMessageListener((MessageListener) v, channel.contains("*") ? new PatternTopic(channel) : new ChannelTopic(channel));
                }
            }
        });
        return container;
    }
}
