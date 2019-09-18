package cn.org.faster.framework.core.cache.service;

import java.util.Collection;
import java.util.Set;

/**
 * @author zhangbowen
 */
public interface ICacheService {
    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @param exp   失效时间(秒)
     */
    void set(String key, String value, long exp);

    /**
     * 删除缓存并获取值
     *
     * @param key 缓存键
     * @return V 泛型
     */
    String deleteAndGet(String key);

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 获取缓存数据,如果关键字不存在返回null
     *
     * @param key 缓存键
     * @return 缓存实体
     */
    String get(String key);

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
     * 是否存在key
     *
     * @param key 键
     * @return true/false
     */
    boolean existKey(String key);

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
    Collection<String> values(String cachePrefix);
}
