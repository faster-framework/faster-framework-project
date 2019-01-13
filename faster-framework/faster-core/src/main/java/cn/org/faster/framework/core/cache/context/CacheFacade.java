package cn.org.faster.framework.core.cache.context;


import cn.org.faster.framework.core.cache.service.ICacheService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author zhangbowen
 */
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
        cacheService.set(prefix + key, JSON.toJSONString(value), exp);
    }

    /**
     * 删除缓存数据
     *
     * @param key 缓存键
     * @return V 泛型
     */
    public static String delete(String key) {
        String value = cacheService.delete(prefix + key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, String.class);
    }

    /**
     * 删除缓存数据
     *
     * @param <V>           泛型
     * @param key           缓存键
     * @param typeReference 类型
     * @return V 泛型
     */
    public static <V> V delete(String key, TypeReference typeReference) {
        String value = cacheService.delete(prefix + key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, typeReference.getType());
    }

    /**
     * 获取缓存对象,返回string
     *
     * @param key 缓存键
     * @return 返回缓存实体
     */
    public static String get(String key) {
        String value = cacheService.get(prefix + key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, String.class);
    }


    /**
     * 获取缓存对象
     *
     * @param <V>           泛型
     * @param key           缓存键
     * @param typeReference 类型
     * @return 返回缓存实体
     */
    public static <V> V get(String key, TypeReference typeReference) {
        String value = cacheService.get(prefix + key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, typeReference.getType());
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
     * @return 返回缓存列表
     */
    public static Set<String> keys(String cachePrefix) {
        return cacheService.keys(prefix + cachePrefix);
    }

    /**
     * 获取以cachePrefix开头的缓存值
     *
     * @param cachePrefix 缓存前缀
     * @return 返回缓存列表
     */
    public static Collection<String> values(String cachePrefix) {
        List<String> resultList = new ArrayList<>();
        Collection<String> values = cacheService.values(prefix + cachePrefix);
        values.forEach(item -> {
            resultList.add(JSON.parseObject(item, String.class));
        });
        return resultList;
    }

    /**
     * 获取以cachePrefix开头的缓存值
     *
     * @param cachePrefix   缓存前缀
     * @param <V>           泛型
     * @param typeReference 类型
     * @return 返回缓存列表
     */
    public static <V> Collection<V> values(String cachePrefix, TypeReference<V> typeReference) {
        List<V> resultList = new ArrayList<>();
        Collection<String> values = cacheService.values(prefix + cachePrefix);
        values.forEach(item -> {
            resultList.add(JSON.parseObject(item, typeReference.getType()));
        });
        return resultList;
    }
}
