package cn.org.faster.framework.auth.spring.boot.autoconfigure;

import cn.org.faster.framework.auth.AuthInterceptor;
import cn.org.faster.framework.auth.AuthService;
import cn.org.faster.framework.web.spring.boot.autoconfigure.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhangbowen
 * @since 2019-11-20
 */
@Configuration
@ConditionalOnProperty(prefix = "app.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(AuthConfiguration.InterceptorConfiguration.class)
@EnableConfigurationProperties({AuthProperties.class, ProjectProperties.class})
public class AuthConfiguration {
    @Autowired
    private AuthProperties authProperties;

    @Bean
    @ConditionalOnMissingBean
    public AuthService authService() {
        AuthService authService = new AuthService();
        authService.setMultipartTerminal(authProperties.isMultipartTerminal());
        return authService;
    }

    @Configuration
    public static class InterceptorConfiguration implements WebMvcConfigurer {
        @Autowired
        private AuthService authService;
        @Autowired
        private AuthProperties authProperties;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new AuthInterceptor(authService))
                    .addPathPatterns(authProperties.getPathPatterns())
                    .excludePathPatterns(authProperties.getExcludePathPatterns()).order(-98);
        }
    }
}
