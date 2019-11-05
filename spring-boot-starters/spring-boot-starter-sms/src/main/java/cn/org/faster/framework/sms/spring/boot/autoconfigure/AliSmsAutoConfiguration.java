package cn.org.faster.framework.sms.spring.boot.autoconfigure;

import cn.org.faster.framework.sms.modules.sms.service.ISmsService;
import cn.org.faster.framework.sms.modules.sms.service.ali.AliSmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author zhangbowen
 * @since 2019-11-04
 */
public class AliSmsAutoConfiguration {
    /**
     * 阿里云短信发送配置
     *
     * @param smsProperties 短信配置
     * @return AliSmsService
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.sms", name = "mode", havingValue = "ali", matchIfMissing = true)
    @ConditionalOnMissingBean
    public ISmsService aliSmsCode(SmsProperties smsProperties) {
        return new AliSmsService(smsProperties.getAli().getAccessKeyId(), smsProperties.getAli().getAccessKeySecret());
    }

}
