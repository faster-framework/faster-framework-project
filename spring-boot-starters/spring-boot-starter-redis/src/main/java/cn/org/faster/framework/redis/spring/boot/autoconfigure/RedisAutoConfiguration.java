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
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author zhangbowen
 * @since 2018/10/10
 */
@Configuration
@Import(RedisCacheAutoConfiguration.class)
@ConditionalOnProperty(prefix = "faster.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public static RedisListenerProcessor redisListenerProcessor(RedisMessageListenerContainer redisMessageListenerContainer) {
        return new RedisListenerProcessor(redisMessageListenerContainer);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }
    @Bean
    public RedisHelper redisHelper(RedisConnectionFactory factory, ObjectMapper objectMapper) {
        return new RedisHelper(factory, objectMapper);
    }
}
