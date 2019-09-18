package cn.org.faster.framework.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * @author zhangbowen
 * @since 2019-06-13
 */
@Slf4j
public class RedisGenericCache extends RedisCache {
    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    public RedisGenericCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
    }

    @Override
    protected Object lookup(Object key) {
        Object value = super.lookup(key);
        if (value != null) {
            log.info("========@Cacheable查询缓存成功========name:{},key:{}", this.getName(), key);
        }
        return value;
    }
}
