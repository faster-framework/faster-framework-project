package cn.org.faster.framework.kafka.spring.boot.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangbowen
 * @since 2018/12/27
 */
@ConfigurationProperties(prefix = "app.kafka")
@Data
public class KafkaProperties {
    /**
     * kafka额外配置开关，true or false
     */
    private boolean enabled;
    /**
     * 全路径
     */
    private String url;
    /**
     * 主机名
     */
    private String host;
    /**
     * 端口号
     */
    private String port;
    /**
     * 生产者
     */
    private Producer producer;
    /**
     * 异常处理器
     */
    private ErrorHandler error;

    /**
     * 生产者配置
     */
    @ConfigurationProperties(prefix = "app.kafka.producer")
    @Data
    public static class Producer {
        /**
         * 是否支持消息回复
         */
        private boolean reply;
        /**
         * 回复主题
         */
        private String replyTopic;
    }

    /**
     * 错误处理器配置
     */
    @ConfigurationProperties(prefix = "app.kafka.error")
    @Data
    public static class ErrorHandler {
        /**
         * 错误处理器配置开关，true or false
         */
        private boolean enabled;
        /**
         * 是否将错误发送到死信 true/false
         */
        private boolean deadLetter;
    }
}
