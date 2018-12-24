package cn.org.faster.framework.web.spring.boot.autoconfigure.secret.advice;

import cn.org.faster.framework.web.secret.model.SecretHttpMessage;
import cn.org.faster.framework.web.secret.properties.SecretProperties;
import cn.org.faster.framework.web.secret.utils.DesCbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * 安全检验，执行于convertMessage之前
 *
 * @author zhangbowen
 * @since 2018/12/13
 */
@Slf4j
@ControllerAdvice
@ConditionalOnProperty(prefix = "faster.secret", name = "enabled", havingValue = "true")
@EnableConfigurationProperties({SecretProperties.class})
@Order(1)
public class SecretRequestAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    private SecretProperties secretProperties;


    /**
     * 是否支持加密消息体
     *
     * @param methodParameter methodParameter
     * @return true/false
     */
    private boolean supportSecretRequest(MethodParameter methodParameter) {
        if (!secretProperties.isScanAnnotation()) {
            return true;
        }
        //判断class是否存在注解
        if (methodParameter.getContainingClass().getAnnotation(secretProperties.getAnnotationClass()) != null) {
            return true;
        }
        //判断方法是否存在注解
        return methodParameter.getMethodAnnotation(secretProperties.getAnnotationClass()) != null;
    }


    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        //如果支持加密消息，进行消息解密。
        boolean supportSafeMessage = supportSecretRequest(parameter);
        String httpBody;
        if (supportSafeMessage) {
            httpBody = decryptBody(inputMessage);
            if (httpBody == null) {
                throw new HttpMessageNotReadableException("request body decrypt error");
            }
        } else {
            httpBody = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
        }
        //返回处理后的消息体给messageConvert
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
}
