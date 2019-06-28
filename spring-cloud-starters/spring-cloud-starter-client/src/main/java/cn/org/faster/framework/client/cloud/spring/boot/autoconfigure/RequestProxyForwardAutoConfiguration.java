package cn.org.faster.framework.client.cloud.spring.boot.autoconfigure;

import cn.org.faster.framework.cloud.client.hystrix.TransferHeaderHystrixConcurrencyStrategy;
import cn.org.faster.framework.cloud.client.interceptor.TransferClientIpRequestInterceptor;
import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.ProxyForwardLoadBalancerFeignClient;
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
    public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
                              SpringClientFactory clientFactory, HttpClient httpClient) {
        ApacheHttpClient delegate = new ApacheHttpClient(httpClient);
        return new ProxyForwardLoadBalancerFeignClient(delegate, cachingFactory, clientFactory);
    }

    @Bean
    public TransferHeaderHystrixConcurrencyStrategy transferHeaderHystrixConcurrencyStrategy() {
        return new TransferHeaderHystrixConcurrencyStrategy();
    }

    @Bean
    public TransferClientIpRequestInterceptor transferHeaderRequestInterceptor() {
        return new TransferClientIpRequestInterceptor();
    }
}
