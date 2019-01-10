package cn.org.faster.framework.shiro.spring.boot.autoconfigure;

import cn.org.faster.framework.shiro.cache.ShiroCacheManager;
import cn.org.faster.framework.web.exception.model.BasisErrorCode;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author zhangbowen
 */
@Configuration
@ConditionalOnProperty(prefix = "faster.shiro", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ShiroProperties.class)
public class ShiroConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * @return 缓存管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager() {
        return new ShiroCacheManager();
    }

    /**
     * @param authorizingRealm shiroRealm
     * @return 权限管理器
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityManager securityManager(AuthorizingRealm authorizingRealm) {
        authorizingRealm.setCacheManager(cacheManager());
        authorizingRealm.setAuthorizationCachingEnabled(true);
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(authorizingRealm);
        securityManager.setCacheManager(cacheManager());
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }


    /**
     * @param securityManager 权限管理器
     * @return 开启shiro 注解
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 过滤器
     *
     * @param securityManager 权限管理器
     * @param shiroProperties shiro配置
     * @return 过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroProperties shiroProperties) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = shiroProperties.getFilterChainDefinitionMap();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/media/**", "anon");
        filterChainDefinitionMap.put("/captcha/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @ControllerAdvice
    @Configuration
    public static class ShiroExceptionHandler {
        @ResponseBody
        @ExceptionHandler(value = AuthorizationException.class)
        public Object handleException(AuthorizationException exception) {
            return ResponseErrorEntity.error(BasisErrorCode.PERMISSION_ERROR, HttpStatus.UNAUTHORIZED);
        }
    }
}
