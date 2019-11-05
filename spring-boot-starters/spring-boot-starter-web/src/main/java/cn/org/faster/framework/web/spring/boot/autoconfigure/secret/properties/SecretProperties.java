package cn.org.faster.framework.web.spring.boot.autoconfigure.secret.properties;

import cn.org.faster.framework.web.secret.annotation.SecretBody;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * 加密消息检验配置
 *
 * @author zhangbowen
 * @since 2018/12/13
 */
@ConfigurationProperties(prefix = "app.secret")
@Data
@Component
public class SecretProperties {
    /**
     * 是否开启
     */
    private boolean enabled;

    /**
     * 开启注解扫描，开启后只有存在@SecretBody注解的才可被扫描。
     */
    private boolean scanAnnotation;

    /**
     * 扫描的包，逗号分隔
     */
    private List<String> scanPackages = new ArrayList<>();

    /**
     * 排除扫描的包，逗号分隔
     */
    private List<String> excludePackages = new ArrayList<>();

    /**
     * 3des 密钥长度不得小于24
     */
    private String desSecretKey = "b2c17b46e2b1415392aab5a82869856c";
    /**
     * 3des IV向量必须为8位
     */
    private String desIv = "61960842";

}
