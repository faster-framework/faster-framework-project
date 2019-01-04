package cn.org.faster.framework.web.spring.boot.autoconfigure.secret.properties;

import cn.org.faster.framework.web.secret.annotation.SecretBody;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.annotation.Annotation;

/**
 * 加密消息检验配置
 *
 * @author zhangbowen
 * @since 2018/12/13
 */
@ConfigurationProperties(prefix = "faster.secret")
@Data
public class SecretProperties {
    /**
     * 是否开启
     */
    private boolean enabled;
    /**
     * 是否扫描注解
     */
    private boolean scanAnnotation;
    /**
     * 扫描自定义注解
     */
    private Class<? extends Annotation> annotationClass = SecretBody.class;

    /**
     * 3des 密钥长度不得小于24
     */
    private String desSecretKey = "b2c17b46e2b1415392aab5a82869856c";
    /**
     * 3des IV向量必须为8位
     */
    private String desIv = "61960842";

}
