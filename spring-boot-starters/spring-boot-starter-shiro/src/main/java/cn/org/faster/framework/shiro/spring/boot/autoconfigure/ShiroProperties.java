package cn.org.faster.framework.shiro.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbowen
 * @since 2019/1/9
 */
@Data
@ConfigurationProperties(prefix = "faster.shiro")
public class ShiroProperties {
    /**
     * 是否开启shiro配置
     */
    private boolean enabled;
    /**
     * 过滤
     */
    private Map<String, String> filterChainDefinitionMap = new HashMap<>();
}
