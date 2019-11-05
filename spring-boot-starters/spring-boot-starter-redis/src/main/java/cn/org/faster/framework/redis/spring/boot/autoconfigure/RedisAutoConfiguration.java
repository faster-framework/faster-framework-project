package cn.org.faster.framework.redis.spring.boot.autoconfigure;

import cn.org.faster.framework.redis.processor.RedisListenerProcessor;
import cn.org.faster.framework.redis.utils.RedisHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author zhangbowen
 * @since 2018/10/10
 */
@Configuration
@Import(RedisCacheAutoConfiguration.class)
@ConditionalOnProperty(prefix = "app.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisAutoConfiguration {
    @Bean
    public static RedisListenerProcessor redisListenerProcessor(RedisMessageListenerContainer redisMessageListenerContainer) {
        return new RedisListenerProcessor(redisMessageListenerContainer);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }
    /**
     * RedisTemplate配置
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        return new StringRedisTemplate(factory);
    }

    @Bean
    public RedisHelper redisHelper(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        return new RedisHelper(factory, objectMapper);
    }
}
