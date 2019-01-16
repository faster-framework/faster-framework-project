package cn.org.faster.framework.redis.processor;

import cn.org.faster.framework.redis.annotation.RedisListener;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author zhangbowen
 * @since 2019/1/11
 */
public class RedisListenerProcessor implements BeanPostProcessor {
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public RedisListenerProcessor(RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, final String beanName) {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Map<Method, RedisListener> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<RedisListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method, RedisListener.class));
        annotatedMethods.forEach((method, v) -> {
            MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(bean, method.getName());
            messageListenerAdapter.afterPropertiesSet();
            String[] channels = v.value();
            for (String channel : channels) {
                redisMessageListenerContainer.addMessageListener(messageListenerAdapter, channel.contains("*") ? new PatternTopic(channel) : new ChannelTopic(channel));
            }
        });
        return bean;
    }
}
