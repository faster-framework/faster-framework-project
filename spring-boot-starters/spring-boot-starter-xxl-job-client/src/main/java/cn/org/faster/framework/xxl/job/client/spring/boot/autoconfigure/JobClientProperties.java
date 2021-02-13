package cn.org.faster.framework.xxl.job.client.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2021-02-13 19:04
 */
@Data
@ConfigurationProperties(
        prefix = "app.xxl-job-client"
)
public class JobClientProperties {
    /**
     * 是否开启 true/false
     */
    private boolean enabled;
    /**
     * 执行器名称，区分项目组
     */
    private String executorName;
    /**
     * 调度中心地址
     */
    private String adminAddress;
    /**
     * 客户端ip地址，不填为自动获取
     */
    private String ip;
    /**
     * rpc通信口号，默认9999
     */
    private int port;
    /**
     * 通信token
     */
    private String accessToken;
    /**
     * 客户端日志存放路径
     */
    private String logPath;
    /**
     * 日志保留时间
     */
    private int logRetentionDays;
}
