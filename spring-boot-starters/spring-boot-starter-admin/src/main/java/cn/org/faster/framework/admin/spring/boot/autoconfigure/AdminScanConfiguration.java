package cn.org.faster.framework.admin.spring.boot.autoconfigure;

import cn.org.faster.framework.admin.ScanConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.auth", name = "mode", havingValue = "admin")
@Import({ScanConfiguration.class,ShiroConfiguration.class})
public class AdminScanConfiguration {
}
