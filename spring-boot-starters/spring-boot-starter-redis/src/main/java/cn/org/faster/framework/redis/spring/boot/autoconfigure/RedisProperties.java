package cn.org.faster.framework.redis.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2019/1/11
 */
@ConfigurationProperties(prefix = "app.redis")
@Data
public class RedisProperties {
    /**
     * 是否开启 true/false
     */
    private boolean enabled;
    /**
     * 全路径
     */
    private String url;
    /**
     * 主机名
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * 密码
     */
    private String password;
}
