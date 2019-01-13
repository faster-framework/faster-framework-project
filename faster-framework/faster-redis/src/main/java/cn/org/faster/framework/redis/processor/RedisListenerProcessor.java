package cn.org.faster.framework.redis.processor;

import cn.org.faster.framework.redis.annotation.RedisListener;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbowen
 * @since 2019/1/11
 */
public class RedisListenerProcessor implements BeanPostProcessor {
    private Map<Object, Map<Method, RedisListener>> beanMapList = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, final String beanName) throws BeansException {
        if (bean instanceof RedisMessageListenerContainer) {
            RedisMessageListenerContainer redisMessageListenerContainer = (RedisMessageListenerContainer) bean;
            beanMapList.forEach((listenerBean, annotatedMethods) -> {
                annotatedMethods.forEach((k, v) -> {
                    MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(listenerBean, k.getName());
                    messageListenerAdapter.afterPropertiesSet();
                    String[] channels = v.value();
                    for (String channel : channels) {
                        redisMessageListenerContainer.addMessageListener(messageListenerAdapter, channel.contains("*") ? new PatternTopic(channel) : new ChannelTopic(channel));
                    }
                });
            });
        } else {
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            Map<Method, RedisListener> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                    (MethodIntrospector.MetadataLookup<RedisListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method, RedisListener.class));
            if (annotatedMethods.size() > 0) {
                beanMapList.put(bean, annotatedMethods);
            }
        }
        return bean;
    }
}
