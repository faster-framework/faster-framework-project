package cn.org.faster.framework.web.spring.boot.autoconfigure.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 */
@ConfigurationProperties(prefix = "app.auth")
@Data
public class AuthProperties {
    /**
     * 是否开启
     */
    private boolean enabled = true;

    /**
     * token是否支持多终端，同时受缓存影响
     */
    private boolean multipartTerminal = true;
}
