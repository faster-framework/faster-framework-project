package cn.org.faster.framework.core.cache.entity;

import lombok.Data;

/**
 * @author zhangbowen
 */
@Data
public class LocalCacheEntity {
    //json字符串
    private String value;
    //缓存时间
    private long exp;
    //存入时间
    private long saveTime;
}
