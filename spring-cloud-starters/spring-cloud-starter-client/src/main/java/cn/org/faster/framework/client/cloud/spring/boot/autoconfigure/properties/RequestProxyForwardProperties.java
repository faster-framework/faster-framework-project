package cn.org.faster.framework.client.cloud.spring.boot.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangbowen
 * @since 2019-06-20
 */
@Component
@ConfigurationProperties(prefix = "app.request-proxy-forward")
@Data
public class RequestProxyForwardProperties {
    private boolean enabled;
}
