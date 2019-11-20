package cn.org.faster.framework.web.spring.boot.autoconfigure.auth;

import cn.org.faster.framework.web.auth.AuthService;
import cn.org.faster.framework.web.spring.boot.autoconfigure.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "app.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({AuthProperties.class, ProjectProperties.class})
public class AuthAutoConfiguration {
    @Autowired
    private AuthProperties authProperties;

    @Bean
    @ConditionalOnMissingBean
    public AuthService authService() {
        AuthService authService = new AuthService();
        authService.setMultipartTerminal(authProperties.isMultipartTerminal());
        return authService;
    }

}
