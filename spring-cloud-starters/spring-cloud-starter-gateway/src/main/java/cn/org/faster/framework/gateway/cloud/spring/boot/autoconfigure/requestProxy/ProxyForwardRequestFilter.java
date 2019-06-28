package cn.org.faster.framework.gateway.cloud.spring.boot.autoconfigure.requestProxy;

import cn.org.faster.framework.redis.utils.RedisHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * @author zhangbowen
 * @since 2019-06-21
 */
@Slf4j
public class ProxyForwardRequestFilter implements GlobalFilter, Ordered {
    public static final String CLIENT_PROXY_FORWARD_KEY = "feign-client-proxy-forward";
    public static final int PROXY_FORWARD_REQUEST_FILTER = 10099;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(GATEWAY_SCHEME_PREFIX_ATTR);

        if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }

        //保留原始url
        addOriginalRequestUrl(exchange, url);

        String clientIp = NetworkUtil.getIp(exchange.getRequest());
        //要请求的服务名称
        String clientName = url.getHost();

        //获取代理
        FeignClientProxyForward proxyForward = getProxyForward(clientIp, clientName);

        //如果目标请求未代理，走原请求
        if (proxyForward == null) {
            //不做任何操作
            return chain.filter(exchange);
        }
        //如果被代理，修改代理的ip，重新构建URI
        URI newUrl = replaceHostName(url.toString(), clientName, proxyForward.getTargetIp() + ":" + proxyForward.getTargetPort());

        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newUrl);

        return chain.filter(exchange);
    }

    private URI replaceHostName(String originalUrl, String host, String newHost) {
        String newUrl = originalUrl;
        if (originalUrl.startsWith("lb://")) {
            newUrl = originalUrl.substring(0, 5)
                    + newHost
                    + originalUrl.substring(5 + host.length());
        }
        StringBuffer buffer = new StringBuffer(newUrl);
        if ((newUrl.startsWith("lb://") && newUrl.length() == 5)) {
            buffer.append("/");
        }
        return URI.create("http" + buffer.substring(2));
    }

    /**
     * 根据请求获取该请求的代理，如果为null，请求源路径
     *
     * @return
     */
    private FeignClientProxyForward getProxyForward(String requestIp, String clientName) {
        try {
            BoundHashOperations<String, String, String> boundHashOperations = RedisHelper.template().boundHashOps(CLIENT_PROXY_FORWARD_KEY);
            //获取代理json列表
            String clientProxyTableJson = boundHashOperations.get(requestIp);
            //代理列表为空
            if (StringUtils.isEmpty(clientProxyTableJson)) {
                //走原方法
                return null;
            }
            Map<String, FeignClientProxyForward> clientProxyTable = JSON.parseObject(clientProxyTableJson, new TypeReference<Map<String, FeignClientProxyForward>>() {
            });
            if (CollectionUtils.isEmpty(clientProxyTable)) {
                //走原方法
                return null;
            }
            return clientProxyTable.get(clientName.toLowerCase());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getOrder() {
        return PROXY_FORWARD_REQUEST_FILTER;
    }

}
