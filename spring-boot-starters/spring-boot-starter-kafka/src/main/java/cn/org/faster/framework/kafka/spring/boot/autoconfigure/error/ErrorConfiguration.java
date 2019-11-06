package cn.org.faster.framework.kafka.spring.boot.autoconfigure.error;

import cn.org.faster.framework.kafka.error.deadLetter.KafkaDeadLetterBatchErrorHandler;
import cn.org.faster.framework.kafka.error.deadLetter.KafkaDeadLetterErrorHandler;
import cn.org.faster.framework.kafka.error.normal.KafkaBatchErrorHandler;
import cn.org.faster.framework.kafka.error.normal.KafkaErrorHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.BatchErrorHandler;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.GenericErrorHandler;

/**
 * @author zhangbowen
 * @since 2019/1/3
 */
@ConditionalOnProperty(prefix = "app.kafka.error", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ErrorConfiguration {

    /**
     * @param kafkaTemplate                           操作类
     * @param concurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory
     * @return 死信批量处理器
     */
    @Bean
    @ConditionalOnProperty(prefix = "app.kafka.error", name = "dead-letter", havingValue = "true")
    @ConditionalOnMissingBean
    public GenericErrorHandler kafkaDeadLetterBatchErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate,
                                                                ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory) {
        //此处之所以要获取bean而非获取配置文件进行判断，因为spring-kafka允许注册自定义factory并且设置batchListener为true，此时配置文件参数可为空。
        if (concurrentKafkaListenerContainerFactory.isBatchListener() != null && concurrentKafkaListenerContainerFactory.isBatchListener()) {
            BatchErrorHandler batchErrorHandler = new KafkaDeadLetterBatchErrorHandler(kafkaTemplate);
            concurrentKafkaListenerContainerFactory.setBatchErrorHandler(batchErrorHandler);
            return batchErrorHandler;
        } else {
            ErrorHandler errorHandler = new KafkaDeadLetterErrorHandler(kafkaTemplate);
            concurrentKafkaListenerContainerFactory.setErrorHandler(errorHandler);
            return errorHandler;
        }
    }

    /**
     * @param concurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory
     * @return 普通处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public GenericErrorHandler kafkaBatchErrorHandler(ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory) {
        //此处之所以要获取bean而非获取配置文件进行判断，因为spring-kafka允许注册自定义factory并且设置batchListener为true，此时配置文件参数可为空。
        if (concurrentKafkaListenerContainerFactory.isBatchListener() != null && concurrentKafkaListenerContainerFactory.isBatchListener()) {
            BatchErrorHandler batchErrorHandler = new KafkaBatchErrorHandler();
            concurrentKafkaListenerContainerFactory.setBatchErrorHandler(batchErrorHandler);
            return batchErrorHandler;
        } else {
            ErrorHandler errorHandler = new KafkaErrorHandler();
            concurrentKafkaListenerContainerFactory.setErrorHandler(errorHandler);
            return errorHandler;
        }
    }
}
