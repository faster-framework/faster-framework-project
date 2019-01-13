package cn.org.faster.framework.shiro.cache;

import org.apache.shiro.authz.AuthorizationInfo;
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
    private final ConcurrentMap<String, Cache<Object, AuthorizationInfo>> caches = new ConcurrentHashMap<>();

    @Override
    public Cache<Object, AuthorizationInfo> getCache(String name) throws CacheException {
        Cache<Object, AuthorizationInfo> cache = caches.get(name);
        if (cache == null) {
            cache = new ShiroCache(name);
            caches.put(name, cache);
        }
        return cache;
    }
}
