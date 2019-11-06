package cn.org.faster.framework.dict.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2019-11-05
 */
@ConfigurationProperties(prefix = "app.dict")
@Data
public class DictProperties {
    /**
     * 是否开启缓存
     */
    private boolean cache;
    /**
     * 缓存时间（秒）
     */
    private long cacheTime = 60 * 60 * 24;
}
