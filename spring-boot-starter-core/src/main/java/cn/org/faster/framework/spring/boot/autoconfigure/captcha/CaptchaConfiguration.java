package cn.org.faster.framework.spring.boot.autoconfigure.captcha;

import cn.org.faster.framework.core.captcha.service.ICaptchaService;
import cn.org.faster.framework.core.captcha.service.impl.CaptchaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 */
@Configuration
public class CaptchaConfiguration {

    /**
     * @return 默认的图形验证码
     */
    @Bean
    @ConditionalOnMissingBean(ICaptchaService.class)
    public ICaptchaService defaultCaptcha() {
        return new CaptchaService();
    }
}
