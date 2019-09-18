package cn.org.faster.framework.redis.cache;

import cn.org.faster.framework.core.cache.service.ICacheService;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangbowen
 */
@Data
public class RedisCacheService implements ICacheService {
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void set(String key, String value, long exp) {
        if (exp > -1) {
            stringRedisTemplate.opsForValue().set(key, value, exp, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.opsForValue().set(key, value);
        }
    }

    @Override
    public String deleteAndGet(String key) {
        String value = get(key);
        if (value != null) {
            stringRedisTemplate.opsForValue().getOperations().delete(key);
        }
        return value;
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.opsForValue().getOperations().delete(key);
    }


    @Override
    public String get(String key) {
        return (String) stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void clear(String cachePrefix) {
        Set<String> keys = stringRedisTemplate.keys(cachePrefix + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            stringRedisTemplate.delete(keys);
        }
    }

    @Override
    public int size(String cachePrefix) {
        return keys(cachePrefix).size();
    }

    @Override
    public boolean existKey(String key) {
        return stringRedisTemplate.opsForValue().getOperations().hasKey(key);
    }

    @Override
    public Set<String> keys(String cachePrefix) {
        return stringRedisTemplate.keys(cachePrefix + "*");
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
