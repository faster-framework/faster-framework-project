package cn.org.faster.framework.admin.shiro.cache;

import cn.org.faster.framework.core.cache.context.CacheFacade;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.Set;

/**
 * @author zhangbowen
 */
public class ShiroCache<K, V> implements Cache<K, V> {
    private static final String CACHE_PREFIX = "shiro:";
    private String keyPrefix;

    public ShiroCache(String keyPrefix) {
        this.keyPrefix = CACHE_PREFIX + keyPrefix;
    }

    @Override
    public V get(K k) throws CacheException {
        return CacheFacade.get(keyPrefix + k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        CacheFacade.set(keyPrefix + k.toString(), v, 0);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        return CacheFacade.delete(keyPrefix + k.toString());
    }

    @Override
    public void clear() throws CacheException {
        CacheFacade.clear(keyPrefix);
    }

    @Override
    public int size() {
        return CacheFacade.size(keyPrefix);
    }

    @Override
    public Set<K> keys() {
        return CacheFacade.keys(keyPrefix);
    }

    @Override
    public Collection<V> values() {
        return CacheFacade.values(keyPrefix);
    }
}
