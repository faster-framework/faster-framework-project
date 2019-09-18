package cn.org.faster.framework.redis.cache;

import lombok.Data;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * redis缓存泛型
 */
@Data
public class RedisGenericCacheProcessor implements BeanPostProcessor {
    /**
     * 存储泛型类型，key为cacheName，value为返回泛型
     */
    private Map<String, Type> genericCacheMap = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, final String beanName) {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Map<Method, Cacheable> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<Cacheable>) method -> AnnotatedElementUtils.findMergedAnnotation(method, Cacheable.class));
        annotatedMethods.forEach((method, v) -> {
            for (String cacheName : v.cacheNames()) {
                genericCacheMap.put(cacheName, method.getGenericReturnType());
            }
        });
        return bean;
    }
}
