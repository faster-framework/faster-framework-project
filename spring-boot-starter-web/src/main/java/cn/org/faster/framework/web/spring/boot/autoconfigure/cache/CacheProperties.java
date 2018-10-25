package cn.org.faster.framework.web.spring.boot.autoconfigure.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 */
@ConfigurationProperties(prefix = "faster.cache")
@Data
public class CacheProperties {
    /**
     * 是否开启缓存
     */
    private boolean enabled = true;
    /**
     * 模式(默认local)
     */
    private String mode = "local";

}
