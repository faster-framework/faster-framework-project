package cn.org.faster.framework.sms.spring.boot.autoconfigure.sms;

import cn.org.faster.framework.sms.modules.sms.service.ISmsService;
import cn.org.faster.framework.sms.modules.sms.service.ali.AliSmsService;
import cn.org.faster.framework.sms.modules.smsCode.service.ISmsCaptchaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.sms", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SmsProperties.class})
public class SmsAutoConfiguration {

    /**
     * 阿里云短信发送配置
     *
     * @param smsProperties 短信配置
     * @return AliSmsService
     */
    @Bean
    @ConditionalOnProperty(prefix = "faster.sms", name = "mode", havingValue = "ali", matchIfMissing = true)
    public ISmsService aliSmsCode(SmsProperties smsProperties) {
        return new AliSmsService(smsProperties.getAli().getAccessKeyId(), smsProperties.getAli().getAccessKeySecret());
    }

    /**
     * debug环境下的短信发送
     *
     * @param smsProperties 短信配置
     * @return ISmsCaptchaService
     */
    @Bean
    @ConditionalOnMissingBean(ISmsCaptchaService.class)
    public ISmsCaptchaService debugSmsCode(SmsProperties smsProperties) {
        return new ISmsCaptchaService(smsProperties.isDebug(), smsProperties.getCaptcha().getExpire()) {
            @Override
            protected boolean sendCode(String phone, String code) {
                return true;
            }
        };
    }
}
