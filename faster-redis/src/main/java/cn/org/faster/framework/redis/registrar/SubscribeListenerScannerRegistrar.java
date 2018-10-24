package cn.org.faster.framework.redis.registrar;

import cn.org.faster.framework.redis.annotation.RedisListenerScan;
import cn.org.faster.framework.redis.scan.ClasspathListenerScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author zhangbowen
 * @since 2018/10/19
 */
@Slf4j
public class SubscribeListenerScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(RedisListenerScan.class.getName()));
        if (annotationAttributes == null) {
            log.warn("RedisListenerScan was not found.Please check your configuration.");
            return;
        }
        ClasspathListenerScanner scanner = new ClasspathListenerScanner(registry);
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }
        scanner.setBeanFactory(beanFactory);
        List<String> basePackages = AutoConfigurationPackages.get(this.beanFactory);
        for (String pkg : annotationAttributes.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        Class<? extends Annotation> annotationClass = annotationAttributes.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
        }
        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
