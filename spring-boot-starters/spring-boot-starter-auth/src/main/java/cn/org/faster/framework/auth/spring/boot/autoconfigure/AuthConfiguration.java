package cn.org.faster.framework.auth.spring.boot.autoconfigure;

import cn.org.faster.framework.auth.AuthInterceptor;
import cn.org.faster.framework.web.auth.AuthService;
import cn.org.faster.framework.web.spring.boot.autoconfigure.auth.AuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
public class AuthConfiguration {

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
