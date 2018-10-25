package cn.org.faster.framework.mybatis.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2018/10/10
 */
@ConfigurationProperties(prefix = "spring.datasource.druid")
@Data
public class DruidProperties {
    private String publicKey;
}
