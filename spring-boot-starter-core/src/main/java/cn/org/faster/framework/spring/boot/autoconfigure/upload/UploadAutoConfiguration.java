package cn.org.faster.framework.spring.boot.autoconfigure.upload;

import cn.org.faster.framework.core.upload.service.IUploadService;
import cn.org.faster.framework.core.upload.service.local.LocalUploadService;
import cn.org.faster.framework.spring.boot.autoconfigure.ProjectProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.upload", name = "enabled", havingValue = "true", matchIfMissing = true)
public class UploadAutoConfiguration {

    @Configuration
    @ConditionalOnProperty(prefix = "faster.upload", name = "mode", havingValue = "local", matchIfMissing = true)
    @EnableConfigurationProperties({ProjectProperties.class, UploadProperties.LocalUploadProperties.class})
    @ConditionalOnMissingBean(IUploadService.class)
    public static class LocalUploadConfiguration {

        @Bean
        public IUploadService localUpload(ProjectProperties projectProperties, UploadProperties.LocalUploadProperties localUploadProperties) {
            return new LocalUploadService(localUploadProperties.getFileDir(), localUploadProperties.getUrlPrefix(), projectProperties.getBase64Secret());
        }
    }
}
