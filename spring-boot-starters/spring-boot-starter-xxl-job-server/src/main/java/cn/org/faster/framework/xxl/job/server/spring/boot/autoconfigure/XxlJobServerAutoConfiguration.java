package cn.org.faster.framework.xxl.job.server.spring.boot.autoconfigure;

import cn.org.faster.framework.xxl.job.server.ScanApplication;
import cn.org.faster.framework.xxl.job.server.core.conf.XxlJobAdminConfig;
import cn.org.faster.framework.xxl.job.server.service.XxlJobService;
import cn.org.faster.framework.xxl.job.server.service.impl.XxlJobServiceImpl;
import cn.org.faster.framework.xxl.job.server.spring.boot.autoconfigure.properties.XxlJobAdminProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author zhangbowen
 * @since 2021-02-14 11:16
 */
@Configuration
@Import({ScanApplication.class})
@Slf4j
@EnableConfigurationProperties(XxlJobAdminProperties.class)
public class XxlJobServerAutoConfiguration {
    @Autowired
    private XxlJobAdminProperties xxlJobAdminProperties;

    @Bean
    @ConditionalOnBean(JavaMailSender.class)
    public XxlJobAdminConfig mailAdminConfig(JavaMailSender javaMailSender) {
        XxlJobAdminConfig xxlJobAdminConfig = new XxlJobAdminConfig();
        xxlJobAdminConfig.setAccessToken(xxlJobAdminProperties.getAccessToken());
        xxlJobAdminConfig.setEmailFrom(xxlJobAdminProperties.getEmail().getFrom());
        xxlJobAdminConfig.setI18n(xxlJobAdminProperties.getI18n());
        xxlJobAdminConfig.setLogretentiondays(xxlJobAdminProperties.getLogRetentionDays());
        xxlJobAdminConfig.setTriggerPoolSlowMax(xxlJobAdminProperties.getTriggerPool().getSlowMax());
        xxlJobAdminConfig.setTriggerPoolFastMax(xxlJobAdminProperties.getTriggerPool().getFastMax());
        xxlJobAdminConfig.setMailSender(javaMailSender);
        return xxlJobAdminConfig;
    }


    @Bean
    @ConditionalOnMissingBean(JavaMailSender.class)
    public XxlJobAdminConfig xxlJobAdminConfig() {
        XxlJobAdminConfig xxlJobAdminConfig = new XxlJobAdminConfig();
        xxlJobAdminConfig.setAccessToken(xxlJobAdminProperties.getAccessToken());
        xxlJobAdminConfig.setEmailFrom(xxlJobAdminProperties.getEmail().getFrom());
        xxlJobAdminConfig.setI18n(xxlJobAdminProperties.getI18n());
        xxlJobAdminConfig.setLogretentiondays(xxlJobAdminProperties.getLogRetentionDays());
        xxlJobAdminConfig.setTriggerPoolSlowMax(xxlJobAdminProperties.getTriggerPool().getSlowMax());
        xxlJobAdminConfig.setTriggerPoolFastMax(xxlJobAdminProperties.getTriggerPool().getFastMax());
        return xxlJobAdminConfig;
    }

    @Bean
    public XxlJobService xxlJobService() {
        return new XxlJobServiceImpl();
    }
}
