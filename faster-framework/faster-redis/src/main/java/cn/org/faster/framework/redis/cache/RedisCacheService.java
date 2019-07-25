package cn.org.faster.framework.redis.cache;

import cn.org.faster.framework.core.cache.service.ICacheService;
import cn.org.faster.framework.redis.utils.RedisHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangbowen
 */
public class RedisCacheService implements ICacheService {
    private RedisTemplate<String, String> redisTemplate;

    public RedisCacheService(RedisHelper redisHelper) {
        redisTemplate = RedisHelper.template();
    }

    @Override
    public void set(String key, String value, long exp) {
        if (exp > -1) {
            redisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    @Override
    public String delete(String key) {
        String value = get(key);
        if (value != null) {
            redisTemplate.opsForValue().getOperations().delete(key);
        }
        return value;
    }


    @Override
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void clear(String cachePrefix) {
        Set<String> keys = redisTemplate.keys(cachePrefix + "*");
        if (!CollectionUtils.isEmpty(keys)) {
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
    public Collection<String> values(String cachePrefix) {
        List<String> list = new ArrayList<>();
        Set<String> keys = keys(cachePrefix);
        keys.forEach(k -> {
            String value = get(k);
            if (value != null) {
                list.add(value);
            }
        });
        return list;
    }
}
