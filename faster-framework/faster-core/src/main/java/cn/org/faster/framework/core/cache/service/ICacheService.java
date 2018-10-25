package cn.org.faster.framework.core.cache.service;

import java.util.Collection;
import java.util.Set;

/**
 * @author zhangbowen
 */
public interface ICacheService<V> {
    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @param exp   失效时间(秒)
     */
    void set(String key, V value, long exp);

    /**
     * 删除缓存数据
     *
     * @param key 缓存键
     * @return V 泛型
     */
    V delete(String key);

    /**
     * 获取缓存数据,如果关键字不存在返回null
     *
     * @param key 缓存键
     * @return 缓存实体
     */
    V get(String key);

    /**
     * 清空以cacheService开头的缓存
     *
     * @param cachePrefix 缓存前缀
     */
    void clear(String cachePrefix);

    /**
     * 查询以cachePrefix开头的cache数量
     *
     * @param cachePrefix 缓存前缀
     * @return 数量
     */
    int size(String cachePrefix);

    /**
     * 查询以cachePrefix开头的keys
     *
     * @param cachePrefix 缓存前缀
     * @return 缓存列表
     */
    Set<String> keys(String cachePrefix);

    /**
     * 查询以cachePrefix开头的值列表
     *
     * @param cachePrefix 缓存前缀
     * @return 缓存列表
     */
    Collection<V> values(String cachePrefix);
}
