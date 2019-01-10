package cn.org.faster.framework.web.spring.boot.autoconfigure.captcha;

import cn.org.faster.framework.web.captcha.service.ICaptchaService;
import cn.org.faster.framework.web.captcha.service.impl.CaptchaService;
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
    @ConditionalOnMissingBean
    public ICaptchaService defaultCaptcha() {
        return new CaptchaService();
    }
}
