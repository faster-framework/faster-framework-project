package cn.org.faster.framework.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 */
@ConfigurationProperties(prefix = "faster")
@Data
public class ProjectProperties {
    private String base64Secret = "ZmFzdGVyLWZyYW1ld29yaw==";
}
