package cn.org.faster.framework.grpc.client.factory;

import lombok.Data;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author zhangbowen
 * @since 2019/1/15
 */
@Data
public class ManageChannelFactoryBean implements FactoryBean {
    private Class interfaceClass;
    private BeanFactory beanFactory;

    /**
     * 创建代理类
     *
     * @return 代理类
     */
    @Override
    public Object getObject() {
        ClientFactory clientFactory = beanFactory.getBean(ClientFactory.class);
        return clientFactory.createClientProxy(interfaceClass);
    }

    @Override
    public Class getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
