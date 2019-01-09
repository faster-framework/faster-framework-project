package cn.org.faster.framework.core.cache.context;


import cn.org.faster.framework.core.cache.service.ICacheService;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Set;

/**
 * @author zhangbowen
 */
@SuppressWarnings("unchecked")
public class CacheFacade {
    public static boolean local = true;
    private static ICacheService cacheService;
    private static String prefix = "";

    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存value
     * @param exp   失效时间(秒)
     * @param <V>   泛型
     */
    public static <V> void set(String key, V value, long exp) {
        cacheService.set(prefix + key, value, exp);
    }

    /**
     * 删除缓存数据
     *
     * @param <V> 泛型
     * @param key 缓存键
     * @return V 泛型
     */
    public static <V> V delete(String key) {
        return (V) cacheService.delete(prefix + key);
    }

    /**
     * 获取缓存对象,解析为默认的class对象
     *
     * @param <V> 泛型
     * @param key 缓存键
     * @return 返回缓存实体
     */
    public static <V> V get(String key) {
        return (V) cacheService.get(prefix + key);
    }

    public static CacheFacade initCache(ICacheService cacheService, boolean local, String prefix) {
        CacheFacade.cacheService = cacheService;
        CacheFacade.local = local;
        if (!StringUtils.isEmpty(prefix)) {
            CacheFacade.prefix = prefix + ":";
        }
        return new CacheFacade();
    }

    /**
     * 清空以cachePrefix开头的缓存
     *
     * @param cachePrefix 缓存前缀
     */
    public static void clear(String cachePrefix) {
        cacheService.clear(prefix + cachePrefix);
    }

    /**
     * 获取以cachePrefix开头的缓存数量
     *
     * @param cachePrefix 缓存前缀
     * @return 缓存数量
     */
    public static int size(String cachePrefix) {
        return cacheService.size(prefix + cachePrefix);
    }

    /**
     * 获取以cachePrefix开头的缓存键列表
     *
     * @param cachePrefix 缓存前缀
     * @param <K>         泛型
     * @return 返回缓存列表
     */
    public static <K> Set<K> keys(String cachePrefix) {
        return cacheService.keys(prefix + cachePrefix);
    }

    /**
     * 获取以cachePrefix开头的缓存值
     *
     * @param cachePrefix 缓存前缀
     * @param <V>         泛型
     * @return 返回缓存列表
     */
    public static <V> Collection<V> values(String cachePrefix) {
        return cacheService.values(prefix + cachePrefix);
    }
}
