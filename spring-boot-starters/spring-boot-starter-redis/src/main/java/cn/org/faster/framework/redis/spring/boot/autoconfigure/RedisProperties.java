package cn.org.faster.framework.redis.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2019/1/11
 */
@ConfigurationProperties(prefix = "faster.redis")
@Data
public class RedisProperties {
    /**
     * 是否开启 true/false
     */
    private boolean enabled;
}
