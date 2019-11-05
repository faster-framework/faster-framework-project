package cn.org.faster.framework.web.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangbowen
 */
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class ProjectProperties {
    private String base64Secret = "ZmFzdGVyLWZyYW1ld29yaw==";
}
