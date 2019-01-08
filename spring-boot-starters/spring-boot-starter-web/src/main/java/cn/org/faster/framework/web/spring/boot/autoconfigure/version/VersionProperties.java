package cn.org.faster.framework.web.spring.boot.autoconfigure.version;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2019/1/8
 */
@ConfigurationProperties(prefix = "faster.version")
@Data
public class VersionProperties {
    /**
     * 是否开启版本控制
     */
    private boolean enabled = true;
    /**
     * 最小版本号，小于该版本号返回版本过时。
     */
    private int minimumVersion;

    /**
     * 开启通过包名解析版本号，包名中存在v1、v2时解析。
     */
    private boolean parsePackageVersion = true;
}
