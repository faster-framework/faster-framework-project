package cn.org.faster.framework.kafka.error.normal;

import cn.org.faster.framework.kafka.error.log.KafkaLoggingErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;

/**
 * 消费者错误处理器
 *
 * @author zhangbowen
 * @since 2019/1/3
 */
@Slf4j
public class KafkaErrorHandler implements ErrorHandler, KafkaLoggingErrorHandler {

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
        thrownException.printStackTrace();
        log.error(handleLogMessage(thrownException));
    }
}
