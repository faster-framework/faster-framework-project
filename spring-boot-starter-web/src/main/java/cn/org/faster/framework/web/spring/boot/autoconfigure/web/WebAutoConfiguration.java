package cn.org.faster.framework.web.spring.boot.autoconfigure.web;

import cn.org.faster.framework.core.utils.time.LocalDateFormatter;
import cn.org.faster.framework.core.utils.time.LocalDatetimeFormatter;
import cn.org.faster.framework.web.exception.GlobalExceptionHandler;
import cn.org.faster.framework.web.spring.boot.autoconfigure.ProjectProperties;
import cn.org.faster.framework.web.web.context.SpringAppContextFacade;
import cn.org.faster.framework.web.web.inteceptor.ContextInterceptor;
import cn.org.faster.framework.web.web.inteceptor.LogInterceptor;
import cn.org.faster.framework.web.web.processor.RequestContextBeanFactoryPostProcessor;
import cn.org.faster.framework.web.web.service.JwtService;
import cn.org.faster.framework.web.web.version.ApiRequestMappingHandlerMapping;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zhangbowen
 */
@Configuration
@Import({GlobalExceptionHandler.class})
@EnableConfigurationProperties({ProjectProperties.class})
public class WebAutoConfiguration implements WebMvcConfigurer, WebMvcRegistrations {
    @Value("${spring.profiles.active}")
    private String env;

    @Bean
    public JwtService jwtService(ProjectProperties projectProperties) {
        JwtService jwtService = new JwtService();
        jwtService.setBase64Security(projectProperties.getBase64Secret());
        jwtService.setEnv(env);
        return jwtService;
    }


    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiRequestMappingHandlerMapping();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new ContextInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SpringAppContextFacade springAppContextFacade() {
        return new SpringAppContextFacade();
    }

    @Bean
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
    public Module customModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDatetimeFormatter.LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDatetimeFormatter.LocalDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new LocalDateFormatter.LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateFormatter.LocalDateDeserializer());
        return module;
    }

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConvert() {
        return new LocalDatetimeFormatter.LocalDatetimeConverter();
    }

    @Bean
    public Converter<String, LocalDate> localDateConvert() {
        return new LocalDateFormatter.LocalDateConverter();
    }

    @Bean
    public static RequestContextBeanFactoryPostProcessor requestContextBeanFactoryPostProcessor() {
        return new RequestContextBeanFactoryPostProcessor();
    }
}
