package cn.org.faster.framework.core.cache.entity;

import lombok.Data;

/**
 * @author zhangbowen
 */
@Data
public class LocalCacheEntity<V> {
    //对象
    private V value;
    //缓存时间
    private long exp;
    //存入时间
    private long saveTime;
}
