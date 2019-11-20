package cn.org.faster.framework.app.auth.spring.boot.autoconfigure;

import cn.org.faster.framework.app.auth.AppAuthInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhangbowen
 * @since 2019-11-20
 */
@Configuration
@ConditionalOnProperty(prefix = "app.auth", name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppAuthConfiguration {

    @Configuration
    public static class InterceptorConfiguration implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new AppAuthInterceptor()).addPathPatterns("/**");
        }
    }
}
