package cn.org.faster.framework.xxl.job.server.spring.boot.autoconfigure;

import cn.org.faster.framework.xxl.job.server.ScanApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 * @since 2021-02-14 11:16
 */
@Configuration
@Import({ScanApplication.class})
@Slf4j
public class XxlJobServerAutoConfiguration {
}
