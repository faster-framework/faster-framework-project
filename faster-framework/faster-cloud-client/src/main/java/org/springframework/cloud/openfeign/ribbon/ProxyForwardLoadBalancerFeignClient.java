/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.openfeign.ribbon;

import cn.org.faster.framework.redis.utils.RedisHelper;
import cn.org.faster.framework.web.utils.NetworkUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.netflix.client.ClientException;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import cn.org.faster.framework.cloud.client.model.FeignClientProxyForward;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * @author zhangbowen
 * 设置开启该配置的开关，以便在生产环境不开启代理。
 * 否则，注入原有客户端，不进行覆盖。
 */
public class ProxyForwardLoadBalancerFeignClient implements Client {
    public static final String CLIENT_PROXY_FORWARD_KEY = "feign-client-proxy-forward";

    static final Request.Options DEFAULT_OPTIONS = new Request.Options();

    private final Client delegate;

    private CachingSpringLoadBalancerFactory lbClientFactory;

    private SpringClientFactory clientFactory;

    public ProxyForwardLoadBalancerFeignClient(Client delegate,
                                               CachingSpringLoadBalancerFactory lbClientFactory,
                                               SpringClientFactory clientFactory) {
        this.delegate = delegate;
        this.lbClientFactory = lbClientFactory;
        this.clientFactory = clientFactory;
    }

    static URI cleanUrl(String originalUrl, String host) {
        String newUrl = originalUrl;
        if (originalUrl.startsWith("https://")) {
            newUrl = originalUrl.substring(0, 8)
                    + originalUrl.substring(8 + host.length());
        } else if (originalUrl.startsWith("http")) {
            newUrl = originalUrl.substring(0, 7)
                    + originalUrl.substring(7 + host.length());
        }
        StringBuffer buffer = new StringBuffer(newUrl);
        if ((newUrl.startsWith("https://") && newUrl.length() == 8)
                || (newUrl.startsWith("http://") && newUrl.length() == 7)) {
            buffer.append("/");
        }
        return URI.create(buffer.toString());
    }

    static URI replaceHostName(String originalUrl, String host, String newHost) {
        String newUrl = originalUrl;
        if (originalUrl.startsWith("https://")) {
            newUrl = originalUrl.substring(0, 8)
                    + newHost
                    + originalUrl.substring(8 + host.length());
        } else if (originalUrl.startsWith("http")) {
            newUrl = originalUrl.substring(0, 7)
                    + newHost
                    + originalUrl.substring(7 + host.length());
        }
        StringBuffer buffer = new StringBuffer(newUrl);
        if ((newUrl.startsWith("https://") && newUrl.length() == 8)
                || (newUrl.startsWith("http://") && newUrl.length() == 7)) {
            buffer.append("/");
        }
        return URI.create(buffer.toString());
    }

    /**
     * 根据请求获取该请求的代理，如果为null，请求源路径
     *
     * @param request
     * @return
     */
    private FeignClientProxyForward getProxyForward(Request request, String clientName) {
        try {
            String requestIp = request.headers().getOrDefault(NetworkUtil.HEADER_X_FORWARDED_FOR, Collections.emptyList()).iterator().next();
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
    public Response execute(Request request, Request.Options options) throws IOException {
        //获取当前请求方的ip。
        //根据ip获取该ip设置的代理列表，判断是否存在当前clientName。
        //如果存在，重写url，转向访问代理。
        try {
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            //判断该次请求的目标服务是否代理
            FeignClientProxyForward proxyForward = getProxyForward(request, clientName);
            //如果目标请求未代理，走原请求
            if (proxyForward == null) {
                //走原方法
                return original(request, options);
            }
            //如果被代理，修改代理的ip，重新构建URI
            URI uriWithoutHost = replaceHostName(request.url(), clientName, proxyForward.getTargetIp() + ":" + proxyForward.getTargetPort());
            FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                    this.delegate, request, uriWithoutHost);
            IClientConfig requestConfig = getClientConfig(options, clientName);
            return lbClient(clientName)
                    .executeWithLoadBalancer(ribbonRequest, requestConfig).toResponse();
        } catch (ClientException e) {
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        }
    }

    public Response original(Request request, Request.Options options) throws IOException {
        try {
            URI asUri = URI.create(request.url());
            String clientName = asUri.getHost();
            URI uriWithoutHost = cleanUrl(request.url(), clientName);
            FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                    this.delegate, request, uriWithoutHost);

            IClientConfig requestConfig = getClientConfig(options, clientName);
            return lbClient(clientName)
                    .executeWithLoadBalancer(ribbonRequest, requestConfig).toResponse();
        } catch (ClientException e) {
            IOException io = findIOException(e);
            if (io != null) {
                throw io;
            }
            throw new RuntimeException(e);
        }
    }


    IClientConfig getClientConfig(Request.Options options, String clientName) {
        IClientConfig requestConfig;
        if (options == DEFAULT_OPTIONS) {
            requestConfig = this.clientFactory.getClientConfig(clientName);
        } else {
            requestConfig = new FeignOptionsClientConfig(options);
        }
        return requestConfig;
    }

    protected IOException findIOException(Throwable t) {
        if (t == null) {
            return null;
        }
        if (t instanceof IOException) {
            return (IOException) t;
        }
        return findIOException(t.getCause());
    }

    public Client getDelegate() {
        return this.delegate;
    }

    private FeignLoadBalancer lbClient(String clientName) {
        return this.lbClientFactory.create(clientName);
    }

    static class FeignOptionsClientConfig extends DefaultClientConfigImpl {

        FeignOptionsClientConfig(Request.Options options) {
            setProperty(CommonClientConfigKey.ConnectTimeout,
                    options.connectTimeoutMillis());
            setProperty(CommonClientConfigKey.ReadTimeout, options.readTimeoutMillis());
        }

        @Override
        public void loadProperties(String clientName) {

        }

        @Override
        public void loadDefaultValues() {

        }

    }

}
