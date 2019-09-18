package cn.org.faster.framework.core.cache.service;

import cn.org.faster.framework.core.cache.entity.LocalCacheEntity;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhangbowen
 */
public class LocalCacheService implements ICacheService {
    private Map<String, LocalCacheEntity> softHashMap = new ConcurrentReferenceHashMap<>();

    @Override
    public void set(String key, String value, long exp) {
        LocalCacheEntity localCacheEntity = new LocalCacheEntity();
        localCacheEntity.setValue(value);
        localCacheEntity.setSaveTime(System.currentTimeMillis());
        localCacheEntity.setExp(exp);
        softHashMap.put(key, localCacheEntity);
    }

    @Override
    public String deleteAndGet(String key) {
        String value = get(key);
        if (value != null) {
            softHashMap.remove(key);
        }
        return value;
    }

    @Override
    public void delete(String key) {
        softHashMap.remove(key);
    }

    @Override
    public String get(String key) {
        LocalCacheEntity localCacheEntity = softHashMap.get(key);
        //说明没过期
        if (localCacheEntity != null && (localCacheEntity.getExp() <= 0 || ((System.currentTimeMillis() - localCacheEntity.getSaveTime()) <= localCacheEntity.getExp() * 1000))) {
            return localCacheEntity.getValue();
        }
        softHashMap.remove(key);
        return null;
    }

    @Override
    public void clear(String cachePrefix) {
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                softHashMap.remove(k);
            }
        });
    }

    @Override
    public int size(String cachePrefix) {
        AtomicInteger size = new AtomicInteger(0);
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                size.getAndIncrement();
            }
        });
        return size.get();
    }

    @Override
    public boolean existKey(String key) {
        return softHashMap.containsKey(key);
    }

    @Override
    public Set<String> keys(String cachePrefix) {
        return softHashMap.keySet().stream().filter(k -> k.startsWith(cachePrefix)).collect(Collectors.toSet());
    }

    @Override
    public Collection<String> values(String cachePrefix) {
        List<String> values = new ArrayList<>();
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                String value = get(k);
                if (value != null) {
                    values.add(value);
                }
            }
        });
        return values;
    }
}
