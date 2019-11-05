package cn.org.faster.framework.dict.spring.boot.autoconfigure;

import cn.org.faster.framework.dict.facade.DictFacade;
import cn.org.faster.framework.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 * @since 2019-11-05
 */
@Configuration
@EnableConfigurationProperties({DictProperties.class})
public class DictAutoConfiguration {
    @Autowired
    private DictProperties dictProperties;

    /**
     * @return 字典service
     */
    @Bean
    @ConditionalOnMissingBean
    public DictService dictService() {
        return new DictService();
    }

    /**
     * @param dictService 字典服务
     * @return 字典门面操作类
     */
    @Bean
    @ConditionalOnMissingBean
    public DictFacade dictFacade(DictService dictService) {
        return new DictFacade(dictProperties.isCache(), dictService);
    }
}
