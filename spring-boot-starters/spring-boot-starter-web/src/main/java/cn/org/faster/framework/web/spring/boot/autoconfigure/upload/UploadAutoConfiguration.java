package cn.org.faster.framework.web.spring.boot.autoconfigure.upload;

import cn.org.faster.framework.web.spring.boot.autoconfigure.ProjectProperties;
import cn.org.faster.framework.web.upload.service.IUploadService;
import cn.org.faster.framework.web.upload.service.local.LocalUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "app.upload", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ProjectProperties.class, UploadProperties.LocalUploadProperties.class})
public class UploadAutoConfiguration {
    @Configuration
    public static class WebAutoConfiguration implements WebMvcConfigurer {
        @Autowired
        private UploadProperties.LocalUploadProperties uploadProperties;
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry
                    .addResourceHandler("/media/**")
                    .addResourceLocations("file:" + uploadProperties.getFileDir() + "/");

        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "app.upload", name = "mode", havingValue = "local", matchIfMissing = true)
    public static class LocalUploadConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public IUploadService localUpload(ProjectProperties projectProperties, UploadProperties.LocalUploadProperties localUploadProperties) {
            return new LocalUploadService(localUploadProperties.getFileDir(), localUploadProperties.getUrlPrefix(), projectProperties.getBase64Secret());
        }
    }
}
