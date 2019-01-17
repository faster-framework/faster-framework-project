package cn.org.faster.framework.grpc.client.scanner;

import cn.org.faster.framework.grpc.client.factory.ManageChannelFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Arrays;
import java.util.Set;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
@Slf4j
public class ClassPathGRpcServiceScanner extends ClassPathBeanDefinitionScanner {
    private final BeanFactory beanFactory;

    public ClassPathGRpcServiceScanner(BeanDefinitionRegistry registry, BeanFactory beanFactory) {
        super(registry, false);
        this.beanFactory = beanFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            logger.warn("No GRpc Service was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    /**
     * 设置bean工厂类
     *
     * @param beanDefinitions
     */
    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitions) {
            definition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
            definition.getPropertyValues().add("interfaceClass", definition.getBeanClassName());
            definition.getPropertyValues().add("beanFactory", beanFactory);
            definition.setBeanClass(ManageChannelFactoryBean.class);
        }
    }


}
