package cn.org.faster.framework.xxl.job.client.spring.boot.autoconfigure;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 * @since 2021-02-13 19:04
 */
@Configuration
@EnableConfigurationProperties(JobClientProperties.class)
@ConditionalOnProperty(prefix = "app.xxl-job-client", name = "enabled", havingValue = "true")
public class JobClientAutoConfiguration {
    /**
     * 客户端启动
     *
     * @param jobClientProperties
     * @return
     */
    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor(JobClientProperties jobClientProperties) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(jobClientProperties.getAdminAddress());
        xxlJobSpringExecutor.setAppname(jobClientProperties.getExecutorName());
        xxlJobSpringExecutor.setAccessToken(jobClientProperties.getAccessToken());
        xxlJobSpringExecutor.setLogRetentionDays(jobClientProperties.getLogRetentionDays());
        xxlJobSpringExecutor.setIp(jobClientProperties.getIp());
        xxlJobSpringExecutor.setPort(jobClientProperties.getPort());
        xxlJobSpringExecutor.setLogPath(jobClientProperties.getLogPath());
        return xxlJobSpringExecutor;
    }
}
