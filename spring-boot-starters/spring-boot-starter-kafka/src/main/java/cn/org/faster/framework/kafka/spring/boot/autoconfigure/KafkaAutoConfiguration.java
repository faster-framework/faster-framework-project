package cn.org.faster.framework.kafka.spring.boot.autoconfigure;

import cn.org.faster.framework.kafka.spring.boot.autoconfigure.error.ErrorConfiguration;
import cn.org.faster.framework.kafka.spring.boot.autoconfigure.reply.ReplyConfiguration;
import cn.org.faster.framework.kafka.spring.boot.autoconfigure.validation.ValidationConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 * @since 2018/12/27
 */
@Configuration
@EnableConfigurationProperties(FastKafkaProperties.class)
@ConditionalOnProperty(prefix = "faster.kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({ValidationConfiguration.class, ReplyConfiguration.class, ErrorConfiguration.class})
public class KafkaAutoConfiguration {
}
