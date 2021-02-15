package cn.org.faster.framework.xxl.job.server.spring.boot.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2021-02-14 20:41
 */
@ConfigurationProperties(prefix = "app.xxl-job.admin")
@Data
public class XxlJobAdminProperties {
    /**
     * 调度中心国际化配置 [必填]： 默认为 "zh_CN"/中文简体, 可选范围为 "zh_CN"/中文简体, "zh_TC"/中文繁体 and "en"/英文
     */
    private String i18n;
    /**
     * 调度中心通讯TOKEN [选填]：非空时启用；
     */
    private String accessToken;
    /**
     * 报警邮箱
     */
    private Email email = new Email();
    /**
     * 调度线程池最大线程配置【必填】
     */
    private TriggerPool triggerPool = new TriggerPool();
    /**
     * 调度中心日志表数据保存天数 [必填]：过期日志自动清理；限制大于等于7时生效，否则, 如-1，关闭自动清理功能；
     */
    private int logRetentionDays;

    @Data
    public static class Email {
        private String from;
    }

    @Data
    public static class TriggerPool {
        private int slowMax;
        private int fastMax;
    }

}
