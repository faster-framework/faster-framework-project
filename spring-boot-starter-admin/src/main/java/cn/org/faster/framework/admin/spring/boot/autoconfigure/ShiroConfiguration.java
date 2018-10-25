package cn.org.faster.framework.admin.spring.boot.autoconfigure;

import cn.org.faster.framework.admin.auth.error.AuthError;
import cn.org.faster.framework.admin.shiro.ShiroFilter;
import cn.org.faster.framework.admin.shiro.cache.ShiroCacheManager;
import cn.org.faster.framework.web.exception.model.ErrorResponseEntity;
import cn.org.faster.framework.web.exception.model.ResultError;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zhangbowen
 */
public class ShiroConfiguration {
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * @return 缓存管理器
     */
    @Bean
    public CacheManager cacheManager() {
        return new ShiroCacheManager();
    }

    /**
     * @param authorizingRealm shiroRealm
     * @return 权限管理器
     */
    @Bean
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
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 拦截器
     *
     * @param securityManager 权限管理器
     * @return 过滤器
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/media/**", "anon");
        filterChainDefinitionMap.put("/captcha/**", "anon");
        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwt", new ShiroFilter());
        shiroFilterFactoryBean.setFilters(filters);
        return shiroFilterFactoryBean;
    }

    @ControllerAdvice
    @Configuration
    public static class ShiroExceptionHandler {
        @ResponseBody
        @ExceptionHandler(value = AuthorizationException.class)
        public Object handleException(AuthorizationException exception) {
            ResultError resultMsg = new ResultError(AuthError.NOT_HAVE_PERMISSION.getValue(), AuthError.NOT_HAVE_PERMISSION.getDescription() + "：" + exception.getMessage());
            return ErrorResponseEntity.error(resultMsg, HttpStatus.BAD_REQUEST);
        }
    }
}
