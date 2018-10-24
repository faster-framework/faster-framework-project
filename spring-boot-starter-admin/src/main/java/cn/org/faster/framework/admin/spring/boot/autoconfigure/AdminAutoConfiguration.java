package cn.org.faster.framework.admin.spring.boot.autoconfigure;

import cn.org.faster.framework.admin.AdminConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.auth", name = "mode", havingValue = "admin")
@Import(AdminConfiguration.class)
public class AdminAutoConfiguration {
}
