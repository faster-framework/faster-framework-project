package cn.org.faster.framework.dict.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangbowen
 * @since 2019-11-05
 */
@Component
@ConfigurationProperties(prefix = "app.dict")
@Data
public class DictProperties {
    /**
     * 是否开启缓存
     */
    private boolean cache;
}
