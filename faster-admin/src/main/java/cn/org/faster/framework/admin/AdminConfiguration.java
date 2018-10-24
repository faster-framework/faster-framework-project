package cn.org.faster.framework.admin;

import cn.org.faster.framework.admin.shiro.ShiroConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 */
@Configuration
@ComponentScan
@Import(ShiroConfiguration.class)
public class AdminConfiguration {
}
