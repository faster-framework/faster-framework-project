package cn.org.faster.framework.sms.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2018/8/27
 */
@Data
@ConfigurationProperties(prefix = "faster.sms")
public class SmsProperties {
    /**
     * 是否开启
     */
    private boolean enabled = true;
    /**
     * 是否为调试环境
     */
    private boolean debug;
    /**
     * 模式("默认ali")
     */
    private String mode = "ali";
    /**
     * 阿里短信配置
     */
    private AliProperties ali = new AliProperties();
    /**
     * 验证码
     */
    private SmsCaptchaProperties captcha = new SmsCaptchaProperties();

    @Data
    @ConfigurationProperties(prefix = "faster.sms.ali")
    public static class AliProperties {
        /**
         * 阿里key
         */
        private String accessKeyId;
        /**
         * 阿里secret
         */
        private String accessKeySecret;
    }

    @Data
    @ConfigurationProperties(prefix = "faster.sms.captcha")
    public static class SmsCaptchaProperties {
        /**
         * 超时时间（秒），默认15分钟
         */
        private long expire = 60 * 15;
    }
}
