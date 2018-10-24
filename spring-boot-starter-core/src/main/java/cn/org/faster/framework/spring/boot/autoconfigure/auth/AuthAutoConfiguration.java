package cn.org.faster.framework.spring.boot.autoconfigure.auth;

import cn.org.faster.framework.core.auth.AuthService;
import cn.org.faster.framework.core.auth.app.interceptor.AppAuthInterceptor;
import cn.org.faster.framework.spring.boot.autoconfigure.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({AuthProperties.class, ProjectProperties.class})
public class AuthAutoConfiguration {
    @Autowired
    private AuthProperties authProperties;

    @Bean
    public AuthService authService() {
        AuthService authService = new AuthService();
        authService.setMultipartTerminal(authProperties.isMultipartTerminal());
        return authService;
    }

    @Configuration
    @ConditionalOnProperty(prefix = "faster.auth", name = "mode", havingValue = "app", matchIfMissing = true)
    public static class AppAuthConfiguration {
        @Configuration
        public static class InterceptorConfiguration implements WebMvcConfigurer {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new AppAuthInterceptor()).addPathPatterns("/**");
            }
        }
    }
}
