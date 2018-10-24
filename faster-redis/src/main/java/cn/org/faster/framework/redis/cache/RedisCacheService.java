package cn.org.faster.framework.redis.cache;

import cn.org.faster.framework.core.cache.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangbowen
 */
public class RedisCacheService<V> implements ICacheService<V> {
    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    @Override
    public void set(String key, V value, long exp) {
        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
    }

    @Override
    public V delete(String key) {
        V value = get(key);
        if (value != null) {
            redisTemplate.opsForValue().getOperations().delete(key);
        }
        return value;
    }

    @Override
    public V get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void clear(String cachePrefix) {
        Set<String> keys = redisTemplate.keys(cachePrefix + "*");
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public int size(String cachePrefix) {
        return keys(cachePrefix).size();
    }

    @Override
    public Set<String> keys(String cachePrefix) {
        return redisTemplate.keys(cachePrefix + "*");
    }

    @Override
    public Collection<V> values(String cachePrefix) {
        List<V> list = new ArrayList<>();
        Set<String> keys = keys(cachePrefix);
        keys.forEach(k -> {
            V value = get(k);
            if (value != null) {
                list.add(value);
            }
        });
        return list;
    }
}
