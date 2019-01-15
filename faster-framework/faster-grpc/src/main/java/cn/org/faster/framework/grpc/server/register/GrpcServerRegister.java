package cn.org.faster.framework.grpc.server.register;

import cn.org.faster.framework.grpc.server.annotation.GrpcServerScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 扫描Server端流程如下：
 * 1. 扫描该类中带有GrpcMethod的方法，获取注解value值(value为空取方法名称)。根据GrpcApi的scheme及GrpcMethod的value、type来创建BindableService。并将其添加至server监听中。
 * 2. 将带有GrpcApi的代理类绑定在BindableService实现类中，并注入到spring中。
 * 3. 请求到来时，BindableService实现类调用GrpcApi的代理类的方法来实现后续业务。（此处根据返回类型不同，进行不同的响应，如返回stream，则进行流响应，对应type的SERVER_STREAMING）
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

    /**
     * 注册服务端GrpcApi
     *
     * @param annotationAttributes annotationAttributes
     * @param registry             registry
     */
    private void registerGrpcApi(AnnotationAttributes annotationAttributes, BeanDefinitionRegistry registry) {

    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(GrpcServerScan.class.getCanonicalName()));
        if (annotationAttributes == null) {
            log.warn("GrpcScan was not found.Please check your configuration.");
            return;
        }
    }
}
