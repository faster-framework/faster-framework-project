package cn.org.faster.framework.redis.spring.boot.autoconfigure;

import cn.org.faster.framework.core.cache.service.ICacheService;
import cn.org.faster.framework.redis.cache.RedisCacheService;
import cn.org.faster.framework.redis.cache.RedisGenericCacheManager;
import cn.org.faster.framework.redis.cache.RedisGenericCacheProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/10/22
 */
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheAutoConfiguration extends CachingConfigurerSupport {
    /**
     * 生成key的策略
     *
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName())
                    .append(".")
                    .append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
    @Bean
    public RedisGenericCacheProcessor redisGenericCacheProcessor() {
        return new RedisGenericCacheProcessor();
    }
    @Bean
    @ConditionalOnMissingBean
    public CacheManagerCustomizers cacheManagerCustomizers(
            ObjectProvider<List<CacheManagerCustomizer<?>>> customizers) {
        return new CacheManagerCustomizers(customizers.getIfAvailable());
    }
    /**
     * 管理缓存
     *
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
                                     RedisGenericCacheProcessor redisGenericCacheProcessor,
                                     ObjectMapper objectMapper,
                                     CacheProperties cacheProperties,
                                     CacheManagerCustomizers customizerInvoker,
                                     ResourceLoader resourceLoader
    ) {

        RedisGenericCacheManager redisGenericCacheManager = new RedisGenericCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                determineConfiguration(resourceLoader.getClassLoader(), cacheProperties));
        redisGenericCacheManager.setCacheProperties(cacheProperties);
        redisGenericCacheManager.setGenericCacheMap(redisGenericCacheProcessor.getGenericCacheMap());
        redisGenericCacheManager.setObjectMapper(objectMapper);
        return customizerInvoker.customize(redisGenericCacheManager);
    }
    private org.springframework.data.redis.cache.RedisCacheConfiguration determineConfiguration(
            ClassLoader classLoader,
            CacheProperties cacheProperties
    ) {
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();
        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
                .defaultCacheConfig();
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new JdkSerializationRedisSerializer(classLoader)));
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixKeysWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }

    /**
     * @return redis缓存
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.cache", name = "mode", havingValue = "redis")
    @ConditionalOnMissingBean(ICacheService.class)
    public ICacheService redisCache(StringRedisTemplate stringRedisTemplate) {
        RedisCacheService redisCacheService = new RedisCacheService();
        redisCacheService.setStringRedisTemplate(stringRedisTemplate);
        return redisCacheService;
    }
}
