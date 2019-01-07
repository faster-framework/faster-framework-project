package cn.org.faster.framework.kafka.spring.boot.autoconfigure.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * consumer消息体验证
 * 使用@Valid注解
 * @author zhangbowen
 * @since 2018/12/25
 */
public class ValidationConfiguration implements KafkaListenerConfigurer {
    @Autowired
    private LocalValidatorFactoryBean validator;

    @Override
    public void configureKafkaListeners(KafkaListenerEndpointRegistrar registrar) {
        registrar.setValidator(validator);
    }
}
