package cn.org.faster.framework.redis.spring.boot.autoconfigure;

import cn.org.faster.framework.core.cache.service.ICacheService;
import cn.org.faster.framework.redis.cache.RedisCacheService;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 * @since 2018/10/22
 */
@Configuration
@ConditionalOnBean(CacheAutoConfiguration.class)
public class RedisCacheAutoConfiguration {
    /**
     * @return redis缓存
     */
    @ConditionalOnMissingBean(ICacheService.class)
    @ConditionalOnProperty(prefix = "faster.cache", name = "mode", havingValue = "redis")
    @Bean
    public ICacheService redisCache() {
        return new RedisCacheService();
    }
}
