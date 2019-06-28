package cn.org.faster.framework.cloud.client.model;

import lombok.Data;

/**
 * @author zhangbowen
 * @since 2019-06-20
 */
@Data
public class FeignClientProxyForward {
    /**
     * 客户端ip
     */
    private String clientIp;
    /**
     * 代理服务
     */
    private String serviceName;
    /**
     * 目标ip
     */
    private String targetIp;
    /**
     * 目标端口
     */
    private String targetPort;
}
