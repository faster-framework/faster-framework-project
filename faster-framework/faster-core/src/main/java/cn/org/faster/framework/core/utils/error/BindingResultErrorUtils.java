package cn.org.faster.framework.core.utils.error;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/1/4
 */
public class BindingResultErrorUtils {

    /**
     * @param bindingResult bindingResult
     * @return bindingResult错误内容
     */
    public static String resolveErrorMessage(BindingResult bindingResult) {
        if (bindingResult == null || CollectionUtils.isEmpty(bindingResult.getFieldErrors())) {
            return "";
        }
        StringBuilder errorMsg = new StringBuilder();
        List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrorList) {
            errorMsg.append("[")
                    .append(fieldError.getField())
                    .append(":")
                    .append(fieldError.getDefaultMessage())
                    .append("]");
        }
        return errorMsg.toString();
    }
}
