package cn.org.faster.framework.kafka.error.deadLetter;

import cn.org.faster.framework.kafka.error.log.KafkaLoggingErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

/**
 * 消费者错误处理器--发送到死信
 * 死信topic = original.DLT
 *
 * @author zhangbowen
 * @since 2019/1/3
 */
@Slf4j
public class KafkaDeadLetterErrorHandler implements ErrorHandler, KafkaLoggingErrorHandler {
    private final DeadLetterPublishingRecoverer deadLetterPublishingRecoverer;

    public KafkaDeadLetterErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
        thrownException.printStackTrace();
        log.error(handleLogMessage(thrownException));
        if (thrownException.getCause() instanceof MethodArgumentNotValidException) {
            return;
        }
        log.error("send failed message to dead letter");
        deadLetterPublishingRecoverer.accept(data, thrownException);
        log.error("send failed message to dead letter successful");
    }
}
