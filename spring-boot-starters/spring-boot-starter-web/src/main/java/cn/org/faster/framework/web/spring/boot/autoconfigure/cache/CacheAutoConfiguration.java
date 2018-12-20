package cn.org.faster.framework.web.spring.boot.autoconfigure.cache;

import cn.org.faster.framework.core.cache.context.CacheFacade;
import cn.org.faster.framework.core.cache.service.ICacheService;
import cn.org.faster.framework.core.cache.service.LocalCacheService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({CacheProperties.class})
public class CacheAutoConfiguration {


    /**
     * @return 本地缓存
     */
    @ConditionalOnProperty(prefix = "faster.cache", name = "mode", havingValue = "local", matchIfMissing = true)
    @Bean
    public ICacheService localCacheService() {
        return new LocalCacheService();
    }

    @Bean
    @ConditionalOnBean(ICacheService.class)
    public CacheFacade initCache(ICacheService cacheService) {
        return CacheFacade.initCache(cacheService, cacheService instanceof LocalCacheService);
    }
}
