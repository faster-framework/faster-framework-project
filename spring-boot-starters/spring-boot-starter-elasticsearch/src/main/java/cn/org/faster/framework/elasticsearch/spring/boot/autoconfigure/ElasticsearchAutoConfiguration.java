package cn.org.faster.framework.elasticsearch.spring.boot.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 * @since 2019-06-17
 */
@Configuration
public class ElasticsearchAutoConfiguration implements InitializingBean {

    static {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @Override
    public void afterPropertiesSet() {

    }
}
