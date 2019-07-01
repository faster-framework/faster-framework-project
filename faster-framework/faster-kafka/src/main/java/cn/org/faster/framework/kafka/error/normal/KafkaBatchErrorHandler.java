package cn.org.faster.framework.kafka.error.normal;

import cn.org.faster.framework.kafka.error.log.KafkaLoggingErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.listener.BatchErrorHandler;

/**
 * 消费者批量错误处理器
 * @author zhangbowen
 * @since 2019/1/3
 */
@Slf4j
public class KafkaBatchErrorHandler implements BatchErrorHandler, KafkaLoggingErrorHandler {

    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data) {
        thrownException.printStackTrace();
        log.error(handleLogMessage(thrownException));
    }
}
