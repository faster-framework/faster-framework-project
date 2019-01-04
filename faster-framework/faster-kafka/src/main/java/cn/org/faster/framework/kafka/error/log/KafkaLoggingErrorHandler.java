package cn.org.faster.framework.kafka.error.log;

import cn.org.faster.framework.core.utils.error.BindingResultErrorUtils;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;

/**
 * 异常信息转为日志
 *
 * @author zhangbowen
 * @since 2019/1/4
 */
public interface KafkaLoggingErrorHandler {
    default String handleLogMessage(Exception thrownException) {
        StringBuilder errorMsg = new StringBuilder();
        if (thrownException.getCause() instanceof MethodArgumentNotValidException) {
            errorMsg.append("kafka listener arguments not valid:");
            MethodArgumentNotValidException methodArgumentNotValidException = ((MethodArgumentNotValidException) thrownException.getCause());
            BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
            if (bindingResult == null) {
                errorMsg.append(methodArgumentNotValidException.getLocalizedMessage());
            } else {
                errorMsg.append(BindingResultErrorUtils.resolveErrorMessage(bindingResult));
            }
        } else {
            errorMsg.append("kafka listener exception:").append(thrownException.getLocalizedMessage());
        }
        return errorMsg.toString();
    }
}
