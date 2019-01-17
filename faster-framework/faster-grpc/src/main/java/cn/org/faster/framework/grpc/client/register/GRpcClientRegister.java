package cn.org.faster.framework.grpc.client.register;

import cn.org.faster.framework.grpc.client.annotation.GRpcClientScan;
import cn.org.faster.framework.grpc.client.annotation.GRpcService;
import cn.org.faster.framework.grpc.client.scanner.ClassPathGRpcServiceScanner;
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

import java.util.List;

/**
 * 扫描Client流程如下：
 * 1. 扫描所有带有GrpcService注解的接口，设置bean定义为动态工厂类ManageChannelFactoryBean，创建代理channel。
 * 2. ManageChannelFactoryBean创建时，根据GrpcService注解获取value服务名，与配置文件中的service列表对应创建channelProxy。
 * 3. ManageChannelFactoryBean创建时获取当前接口下所有方法，并获取GrpcMethod注解值，以此创建call，设置进channelProxy中。
 * 4. 调用方法时，根据方法名获取channelProxy中的call，根据type选择通信方式，请求server端。
 *
 * @author zhangbowen
 * @since 2019/1/15
 */
@Slf4j
public class GRpcClientRegister implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
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
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(GRpcClientScan.class.getCanonicalName()));
        if (annotationAttributes == null) {
            log.warn("GrpcScan was not found.Please check your configuration.");
            return;
        }
        ClassPathGRpcServiceScanner classPathGrpcServiceScanner = new ClassPathGRpcServiceScanner(registry, beanFactory);
        classPathGrpcServiceScanner.setResourceLoader(this.resourceLoader);
        classPathGrpcServiceScanner.addIncludeFilter(new AnnotationTypeFilter(GRpcService.class));
        List<String> basePackages = AutoConfigurationPackages.get(this.beanFactory);
        for (String pkg : annotationAttributes.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        classPathGrpcServiceScanner.doScan(StringUtils.toStringArray(basePackages));
    }

}
