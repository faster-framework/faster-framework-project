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

    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存value
     * @param exp   失效时间(秒)
     * @param <V>   泛型
     */
    public static <V> void set(String key, V value, long exp) {
        cacheService.set(key, JSON.toJSONString(value), exp);
    }

    /**
     * 删除缓存数据
     *
     * @param key 缓存键
     */
    public static void delete(String key) {
        cacheService.delete(key);
    }

    /**
     * 删除缓存并获取值
     *
     * @param key 缓存键
     * @return V 泛型
     */
    public static String deleteAndGet(String key) {
        String value = cacheService.deleteAndGet(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(key, String.class);
    }

    /**
     * 删除缓存数据
     *
     * @param typeReference 泛型
     * @param key 缓存键
     * @param <V> 泛型
     * @return V 泛型
     */
    public static <V> V delete(String key, TypeReference<V> typeReference) {
        String value = cacheService.deleteAndGet(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, typeReference);
    }

    /**
     * 获取缓存对象,返回string
     *
     * @param key 缓存键
     * @return 返回缓存实体
     */
    public static String get(String key) {
        String value = cacheService.get(key);
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
    public static <V> V get(String key, TypeReference<V> typeReference) {
        String value = cacheService.get(key);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return JSON.parseObject(value, typeReference);
    }

    public static CacheFacade initCache(ICacheService cacheService, boolean local) {
        CacheFacade.cacheService = cacheService;
        CacheFacade.local = local;
        return new CacheFacade();
    }

    /**
     * 清空以cachePrefix开头的缓存
     *
     * @param cachePrefix 缓存前缀
     */
    public static void clear(String cachePrefix) {
        cacheService.clear(cachePrefix);
    }

    /**
     * 获取以cachePrefix开头的缓存数量
     *
     * @param cachePrefix 缓存前缀
     * @return 缓存数量
     */
    public static int size(String cachePrefix) {
        return cacheService.size(cachePrefix);
    }


    /**
     * 判断是否存在某个key
     *
     * @param key 缓存键
     * @return true/false
     */
    public static boolean existKey(String key) {
        return cacheService.existKey(key);
    }

    /**
     * 获取以cachePrefix开头的缓存键列表
     *
     * @param cachePrefix 缓存前缀
     * @return 返回缓存列表
     */
    public static Set<String> keys(String cachePrefix) {
        return cacheService.keys(cachePrefix);
    }

    /**
     * 获取以cachePrefix开头的缓存值
     *
     * @param cachePrefix 缓存前缀
     * @return 返回缓存列表
     */
    public static Collection<String> values(String cachePrefix) {
        List<String> resultList = new ArrayList<>();
        Collection<String> values = cacheService.values(cachePrefix);
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
        Collection<String> values = cacheService.values(cachePrefix);
        values.forEach(item -> {
            resultList.add(JSON.parseObject(item, typeReference));
        });
        return resultList;
    }
}
