package cn.org.faster.framework.dict.spring.boot.autoconfigure;

import cn.org.faster.framework.dict.ScanConfiguration;
import cn.org.faster.framework.dict.facade.DictFacade;
import cn.org.faster.framework.dict.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhangbowen
 * @since 2019-11-05
 */
@Configuration
@EnableConfigurationProperties({DictProperties.class})
@Import({ScanConfiguration.class})
public class DictAutoConfiguration {
    @Autowired
    private DictProperties dictProperties;


    /**
     * @param dictService 字典服务
     * @return 字典门面操作类
     */
    @Bean
    @ConditionalOnMissingBean
    public DictFacade dictFacade(DictService dictService) {
        return new DictFacade(dictProperties.isCache(), dictProperties.getCacheTime(), dictService);
    }
}
