package cn.org.faster.framework.mybatis.spring.boot.autoconfigure;

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
        prefix = "app.datasource"
)
public class MybatisProperties {
    /**
     * 全路径
     */
    private String url;
    /**
     * 主机名
     */
    private String host;
    /**
     * 数据库名称
     */
    private String name;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 端口号
     */
    private String port;
}
