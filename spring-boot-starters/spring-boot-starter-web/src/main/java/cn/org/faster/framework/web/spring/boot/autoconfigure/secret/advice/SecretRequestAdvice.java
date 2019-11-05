package cn.org.faster.framework.web.spring.boot.autoconfigure.secret.advice;

import cn.org.faster.framework.web.secret.HttpMessageDecryptException;
import cn.org.faster.framework.web.secret.annotation.SecretBody;
import cn.org.faster.framework.web.secret.model.SecretHttpMessage;
import cn.org.faster.framework.web.secret.utils.DesCbcUtil;
import cn.org.faster.framework.web.spring.boot.autoconfigure.secret.properties.SecretProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 安全检验，执行于convertMessage之前
 *
 * @author zhangbowen
 * @since 2018/12/13
 */
@Slf4j
@ControllerAdvice
@EnableConfigurationProperties({SecretProperties.class})
@Order(1)
@ConditionalOnProperty(prefix = "app.secret", name = "enabled", havingValue = "true")
public class SecretRequestAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    private SecretProperties secretProperties;
    private ClassPathScanningCandidateComponentProvider secretScanner;
    /**
     * 是否扫描全部包
     */
    private boolean isScanAllController = true;
    /**
     * 包含的类
     */
    private Set<String> includeClassList = new TreeSet<>();
    /**
     * 排除的类
     */
    private Set<String> excludeClassList = new TreeSet<>();


    /**
     * 是否支持加密消息体
     *
     * @param methodParameter methodParameter
     * @return true/false
     */
    private boolean supportSecretRequest(MethodParameter methodParameter) {
        Class currentClass = methodParameter.getContainingClass();

        //如果当前类被排除，则不解析
        if (excludeClassList.contains(currentClass.getName())) {
            return false;
        }
        //如果扫描的不是全部，并且当前类不在扫描范围内，排除
        if (!this.isScanAllController && !includeClassList.contains(currentClass.getName())) {
            return false;
        }
        //如果不为注解扫描模式，则返回支持。
        if (!secretProperties.isScanAnnotation()) {
            return true;
        }
        //如果为注解扫描模式，判断
        //类注解
        Annotation classAnnotation = currentClass.getAnnotation(SecretBody.class);
        //方法注解
        SecretBody methodAnnotation = methodParameter.getMethodAnnotation(SecretBody.class);
        //如果类与方法均不存在注解，则排除
        if (classAnnotation == null && methodAnnotation == null) {
            return false;
        }
        //如果当前类的注解含有排除标识，则排除
        if (classAnnotation != null && ((SecretBody) classAnnotation).exclude()) {
            return false;
        }
        //如果当前方法的注解含有排除标识，则排除
        if (methodAnnotation != null && methodAnnotation.exclude()) {
            return false;
        }
        return true;
    }


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return supportSecretRequest(methodParameter);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String httpBody = decryptBody(inputMessage);
        if (httpBody == null) {
            throw new HttpMessageDecryptException("request body decrypt error");
        }
        return new SecretHttpMessage(new ByteArrayInputStream(httpBody.getBytes()), inputMessage.getHeaders());
    }

    /**
     * 解密消息体,3des解析（cbc模式）
     *
     * @param inputMessage 消息体
     * @return 明文
     */
    private String decryptBody(HttpInputMessage inputMessage) throws IOException {
        InputStream encryptStream = inputMessage.getBody();
        String encryptBody = StreamUtils.copyToString(encryptStream, Charset.defaultCharset());
        return DesCbcUtil.decode(encryptBody, secretProperties.getDesSecretKey(), secretProperties.getDesIv());
    }

    private void registerInclude() {
        secretScanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        secretScanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        //根据扫描包获取所有的类
        List<String> scanPackages = secretProperties.getScanPackages();
        if (!CollectionUtils.isEmpty(scanPackages)) {
            isScanAllController = false;
            for (String scanPackage : scanPackages) {
                Set<BeanDefinition> beanDefinitions = secretScanner.findCandidateComponents(scanPackage);
                for (BeanDefinition beanDefinition : beanDefinitions) {
                    includeClassList.add(beanDefinition.getBeanClassName());
                }
            }
        }
    }

    private void registerExclude() {
        secretScanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        secretScanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        //根据扫描包获取所有的类
        List<String> scanPackages = secretProperties.getExcludePackages();
        if (!CollectionUtils.isEmpty(scanPackages)) {
            for (String scanPackage : scanPackages) {
                Set<BeanDefinition> beanDefinitions = secretScanner.findCandidateComponents(scanPackage);
                for (BeanDefinition beanDefinition : beanDefinitions) {
                    excludeClassList.add(beanDefinition.getBeanClassName());
                }
            }
        }
    }

    @PostConstruct
    private void init() {
        secretScanner = new ClassPathScanningCandidateComponentProvider(false);
        registerInclude();
        registerExclude();
    }
}
