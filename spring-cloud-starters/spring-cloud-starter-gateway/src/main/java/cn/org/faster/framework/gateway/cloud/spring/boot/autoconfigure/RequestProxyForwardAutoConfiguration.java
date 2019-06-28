package cn.org.faster.framework.gateway.cloud.spring.boot.autoconfigure;

import cn.org.faster.framework.gateway.cloud.spring.boot.autoconfigure.requestProxy.ProxyForwardRequestFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 * @since 2019-06-20
 */
@Configuration
@ConditionalOnProperty(prefix = "app.request-proxy-forward", name = "enabled", havingValue = "true")
public class RequestProxyForwardAutoConfiguration {

    @Bean
    public ProxyForwardRequestFilter proxyForwardRequestFilter() {
        return new ProxyForwardRequestFilter();
    }

}
