package cn.org.faster.framework.shiro.cache;

import cn.org.faster.framework.core.cache.context.CacheFacade;
import com.alibaba.fastjson.TypeReference;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhangbowen
 */
@SuppressWarnings("unchecked")
public class ShiroCache implements Cache<Object, AuthorizationInfo> {
    private static final String CACHE_PREFIX = "shiro:";
    private String keyPrefix;

    public ShiroCache(Object keyPrefix) {
        this.keyPrefix = CACHE_PREFIX + keyPrefix;
    }

    @Override
    public AuthorizationInfo put(Object k, AuthorizationInfo authenticationInfo) throws CacheException {
        CacheFacade.set(keyPrefix + k, authenticationInfo, -1);
        return authenticationInfo;
    }

    @Override
    public AuthorizationInfo get(Object k) throws CacheException {
        return CacheFacade.get(keyPrefix + k, new TypeReference<SimpleAuthorizationInfo>() {
        });
    }


    @Override
    public AuthorizationInfo remove(Object k) throws CacheException {
        return CacheFacade.delete(keyPrefix + k, new TypeReference<SimpleAuthorizationInfo>() {
        });
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
    public Set<Object> keys() {
        return new HashSet<>(CacheFacade.keys(keyPrefix));
    }

    @Override
    public Collection values() {
        return CacheFacade.values(keyPrefix, new TypeReference<SimpleAuthorizationInfo>() {
        });
    }
}
