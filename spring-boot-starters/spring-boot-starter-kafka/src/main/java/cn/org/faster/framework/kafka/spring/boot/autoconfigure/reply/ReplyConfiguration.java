package cn.org.faster.framework.kafka.spring.boot.autoconfigure.reply;

import cn.org.faster.framework.kafka.spring.boot.autoconfigure.FastKafkaProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

/**
 * 回复型生产者
 *
 * @author zhangbowen
 * @since 2019/1/3
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.kafka.producer", name = "reply", havingValue = "true")
public class ReplyConfiguration {
    private final FastKafkaProperties properties;
    private final KafkaProperties kafkaProperties;
    private final RecordMessageConverter messageConverter;
    private final ConsumerFactory<Object, Object> consumerFactory;
    private final ProducerFactory<Object, Object> producerFactory;
    private final ProducerListener<Object, Object> producerListener;

    public ReplyConfiguration(FastKafkaProperties properties,
                              KafkaProperties kafkaProperties,
                              ObjectProvider<RecordMessageConverter> messageConverter,
                              ConsumerFactory<Object, Object> consumerFactory,
                              ProducerFactory<Object, Object> producerFactory,
                              ProducerListener<Object, Object> producerListener) {
        this.kafkaProperties = kafkaProperties;
        this.properties = properties;
        this.messageConverter = messageConverter.getIfUnique();
        this.consumerFactory = consumerFactory;
        this.producerFactory = producerFactory;
        this.producerListener = producerListener;
    }

    /**
     * @return replyContainer
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaMessageListenerContainer<Object, Object> replyContainer() {
        ContainerProperties containerProperties = new ContainerProperties(properties.getProducer().getReplyTopic());
        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    /**
     * @return 回复操作类
     */
    @Bean
    @ConditionalOnMissingBean
    public ReplyingKafkaTemplate<?, ?, ?> replyingKafkaTemplate() {
        ReplyingKafkaTemplate<Object, Object, Object> kafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, replyContainer());
        if (this.messageConverter != null) {
            kafkaTemplate.setMessageConverter(this.messageConverter);
        }
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.setDefaultTopic(kafkaProperties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }
}
