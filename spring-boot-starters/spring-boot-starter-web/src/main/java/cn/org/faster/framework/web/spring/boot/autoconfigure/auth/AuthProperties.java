package cn.org.faster.framework.web.spring.boot.autoconfigure.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangbowen
 */
@ConfigurationProperties(prefix = "app.auth")
@Data
public class AuthProperties {
    /**
     * 是否开启
     */
    private boolean enabled = true;

    /**
     * token是否支持多终端，同时受缓存影响
     */
    private boolean multipartTerminal = true;
    /**
     * 拦截的路径
     */
    private List<String> pathPatterns = Stream.of("/**").collect(Collectors.toList());
    /**
     * 放过的路径
     */
    private List<String> excludePathPatterns = new ArrayList<>();
}
