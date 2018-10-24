package cn.org.faster.framework.core.web.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author zhangbowen
 */
public class SpringAppContextFacade implements ApplicationContextAware {
    public volatile static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringAppContextFacade.applicationContext = applicationContext;
    }
}
