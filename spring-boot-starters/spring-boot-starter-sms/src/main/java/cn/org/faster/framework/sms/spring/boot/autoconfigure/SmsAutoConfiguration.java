package cn.org.faster.framework.sms.spring.boot.autoconfigure;

import cn.org.faster.framework.sms.modules.smsCode.service.ISmsCaptchaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "app.sms", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SmsProperties.class})
@Import(AliSmsAutoConfiguration.class)
public class SmsAutoConfiguration {

    /**
     * debug环境下的短信发送
     *
     * @param smsProperties 短信配置
     * @return ISmsCaptchaService
     */
    @Bean
    @ConditionalOnMissingBean
    public ISmsCaptchaService debugSmsCode(SmsProperties smsProperties) {
        return new ISmsCaptchaService(smsProperties.isDebug(), smsProperties.getCaptcha().getExpire()) {
            @Override
            protected boolean sendCode(String phone, String code) {
                return true;
            }
        };
    }
}
