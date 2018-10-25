package cn.org.faster.framework.admin.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zhangbowen
 */
@SuppressWarnings("unchecked")
public class ShiroCacheManager implements CacheManager {
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        Cache<K, V> cache = caches.get(name);
        if (cache == null) {
            cache = new ShiroCache<>(name);
            caches.put(name, cache);
        }
        return cache;
    }
}
