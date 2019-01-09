package cn.org.faster.framework.redis.scan;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;

/**
 * @author zhangbowen
 * @since 2018/10/19
 */
public class ClasspathListenerScanner extends ClassPathBeanDefinitionScanner {
    private BeanFactory beanFactory;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ClasspathListenerScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No Redis Subscribe Listener was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
            return beanDefinitions;
        }
        BeanDefinitionRegistry beanDefinitionRegistry = super.getRegistry();
        if (beanDefinitionRegistry == null) {
            return beanDefinitions;
        }
        beanDefinitions.forEach(item -> beanDefinitionRegistry.registerBeanDefinition(item.getBeanName(),
                item.getBeanDefinition())
        );
        return beanDefinitions;
    }
}
