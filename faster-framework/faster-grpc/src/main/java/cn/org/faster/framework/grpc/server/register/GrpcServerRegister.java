package cn.org.faster.framework.grpc.server.register;

import cn.org.faster.framework.grpc.server.annotation.GrpcApi;
import cn.org.faster.framework.grpc.server.annotation.GrpcServerScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 扫描Server端流程如下：
 * <p>
 * 1. scanner扫描带有GrpcApi注解的类，注入到spring中。
 * 2. processor扫描带有GrpcMethod注解的方法，创建ServerCallAdapter（实现了BindableService），放入ServerCallContainer中。
 * 3. 创建ServerCallAdapter时，获取当前方法，放入ServerCallAdapter中，当接收到消息时，调用此方法，传入接收的参数。
 * 4. server启动时，获取serverCallConatiner中的所有BindableService，进行设置。
 *
 * @author zhangbowen
 * @since 2019/1/13
 */
@Slf4j
public class GrpcServerRegister implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private BeanFactory beanFactory;
    private ResourceLoader resourceLoader;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(GrpcServerScan.class.getCanonicalName()));
        if (annotationAttributes == null) {
            log.warn("GrpcScan was not found.Please check your configuration.");
            return;
        }
        ClassPathBeanDefinitionScanner classPathGrpcApiScanner = new ClassPathBeanDefinitionScanner(registry, false);
        classPathGrpcApiScanner.setResourceLoader(this.resourceLoader);
        classPathGrpcApiScanner.addIncludeFilter(new AnnotationTypeFilter(GrpcApi.class));
        List<String> basePackages = AutoConfigurationPackages.get(this.beanFactory);
        for (String pkg : annotationAttributes.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        classPathGrpcApiScanner.scan(StringUtils.toStringArray(basePackages));
    }
}
