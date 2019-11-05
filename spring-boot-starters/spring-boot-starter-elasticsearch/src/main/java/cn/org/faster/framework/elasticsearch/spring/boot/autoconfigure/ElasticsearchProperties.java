package cn.org.faster.framework.elasticsearch.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhangbowen
 * @since 2019-07-27
 */
@Data
@Component
@ConfigurationProperties(
        prefix = "app.elasticsearch"
)
public class ElasticsearchProperties {
    /**
     * 全路径
     */
    private String url;
    /**
     * 集群名称
     */
    private String name;
    /**
     * 主机名
     */
    private String host;
    /**
     * 端口号
     */
    private String port;
}
