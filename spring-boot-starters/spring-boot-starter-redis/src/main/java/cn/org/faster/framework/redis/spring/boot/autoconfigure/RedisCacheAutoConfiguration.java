package cn.org.faster.framework.redis.spring.boot.autoconfigure;

import cn.org.faster.framework.core.cache.service.ICacheService;
import cn.org.faster.framework.redis.cache.RedisCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author zhangbowen
 * @since 2018/10/22
 */
public class RedisCacheAutoConfiguration {
    /**
     * @return redis缓存
     */
    @ConditionalOnProperty(prefix = "faster.cache", name = "mode", havingValue = "redis")
    @Bean
    @ConditionalOnMissingBean(ICacheService.class)
    public ICacheService redisCache() {
        return new RedisCacheService();
    }
}
