package cn.org.faster.framework.client.cloud.spring.boot.autoconfigure;

import cn.org.faster.framework.cloud.client.processor.FeignBeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 * @since 2019/4/29
 */
@Configuration
public class CloudClientAutoConfiguration {
    @Bean
    public FeignBeanFactoryPostProcessor feignBeanFactoryPostProcessor() {
        return new FeignBeanFactoryPostProcessor();
    }
}
