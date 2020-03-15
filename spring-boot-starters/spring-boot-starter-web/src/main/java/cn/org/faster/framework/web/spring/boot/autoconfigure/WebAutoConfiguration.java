package cn.org.faster.framework.web.spring.boot.autoconfigure;

import cn.org.faster.framework.core.utils.time.LocalDateFormatter;
import cn.org.faster.framework.core.utils.time.LocalDatetimeFormatter;
import cn.org.faster.framework.web.context.ContextInterceptor;
import cn.org.faster.framework.web.context.LogInterceptor;
import cn.org.faster.framework.web.context.model.SpringAppContextFacade;
import cn.org.faster.framework.web.context.processor.RequestContextBeanFactoryPostProcessor;
import cn.org.faster.framework.web.exception.handler.ExceptionExecutorHandler;
import cn.org.faster.framework.web.jwt.service.JwtService;
import cn.org.faster.framework.web.spring.boot.autoconfigure.exception.GlobalExceptionHandler;
import cn.org.faster.framework.web.spring.boot.autoconfigure.version.VersionProperties;
import cn.org.faster.framework.web.version.ApiRequestMappingHandlerMapping;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zhangbowen
 */
@Configuration
@Import({GlobalExceptionHandler.class})
@EnableConfigurationProperties({ProjectProperties.class, VersionProperties.class})
@EnableTransactionManagement
@EnableScheduling
public class WebAutoConfiguration implements WebMvcConfigurer, WebMvcRegistrations {
    @Value("${spring.profiles.active:dev}")
    private String env;
    @Autowired
    private VersionProperties versionProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtService jwtService(ProjectProperties projectProperties) {
        JwtService jwtService = new JwtService();
        jwtService.setBase64Security(projectProperties.getBase64Secret());
        jwtService.setEnv(env);
        return jwtService;
    }


    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        if (versionProperties.isEnabled()) {
            return new ApiRequestMappingHandlerMapping(versionProperties.getMinimumVersion(), versionProperties.isParsePackageVersion());
        } else {
            return null;
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**").order(-100);
        registry.addInterceptor(new ContextInterceptor()).addPathPatterns("/**").order(-99);
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringAppContextFacade springAppContextFacade() {
        return new SpringAppContextFacade();
    }

    @Bean
    @ConditionalOnMissingBean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    @ConditionalOnMissingBean
    public Module customModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDatetimeFormatter.LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDatetimeFormatter.LocalDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new LocalDateFormatter.LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateFormatter.LocalDateDeserializer());
        module.addSerializer(Long.class, ToStringSerializer.instance);
        return module;
    }

    @Bean
    @ConditionalOnMissingBean
    public Converter<String, LocalDateTime> localDateTimeConvert() {
        return new LocalDatetimeFormatter.LocalDatetimeConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public Converter<String, LocalDate> localDateConvert() {
        return new LocalDateFormatter.LocalDateConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public static RequestContextBeanFactoryPostProcessor requestContextBeanFactoryPostProcessor() {
        return new RequestContextBeanFactoryPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return true;
            }

            @Override
            public void handleError(ClientHttpResponse response) {

            }
        };
        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionExecutorHandler exceptionExecutorHandler(){
        return new ExceptionExecutorHandler();
    }
}
