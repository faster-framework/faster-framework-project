package cn.org.faster.framework.web.spring.boot.autoconfigure.secret.properties;

import cn.org.faster.framework.web.spring.boot.autoconfigure.secret.annotation.SecretBody;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.lang.annotation.Annotation;

/**
 * 加密消息检验配置
 *
 * @author zhangbowen
 * @since 2018/12/13
 */
@ConfigurationProperties(prefix = "secret")
@Data
public class SecretProperties {
    /**
     * 是否开启
     */
    private boolean enabled = false;
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
    private String desSecretKey = "24ac25e04d9d8e2da25549dc94ecf357";
    /**
     * 3des IV向量必须为8位
     */
    private String desIv = "01234567";

}
