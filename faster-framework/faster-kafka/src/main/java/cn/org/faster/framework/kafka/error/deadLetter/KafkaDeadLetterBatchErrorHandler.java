package cn.org.faster.framework.kafka.error.deadLetter;

import cn.org.faster.framework.kafka.error.log.KafkaLoggingErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

/**
 * 批量消费者错误处理器--发送到死信
 * 死信topic = original.DLT
 *
 * @author zhangbowen
 * @since 2019/1/3
 */
@Slf4j
public class KafkaDeadLetterBatchErrorHandler implements BatchErrorHandler, KafkaLoggingErrorHandler {
    private final DeadLetterPublishingRecoverer deadLetterPublishingRecoverer;

    public KafkaDeadLetterBatchErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecords<?, ?> data) {
        thrownException.printStackTrace();
        log.error(handleLogMessage(thrownException));
        if (thrownException.getCause() instanceof MethodArgumentNotValidException) {
            return;
        }
        Set<TopicPartition> topicPartitions = data.partitions();
        log.error("send failed message to dead letter");
        for (TopicPartition topicPartition : topicPartitions) {
            List<? extends ConsumerRecord<?, ?>> list = data.records(topicPartition);
            for (ConsumerRecord<?, ?> consumerRecord : list) {
                deadLetterPublishingRecoverer.accept(consumerRecord, thrownException);
            }
        }
        log.error("send failed message to dead letter successful");
    }
}
