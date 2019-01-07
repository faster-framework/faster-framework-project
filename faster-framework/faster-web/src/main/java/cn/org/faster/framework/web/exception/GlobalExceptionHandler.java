package cn.org.faster.framework.web.exception;

import cn.org.faster.framework.core.utils.error.BindingResultErrorUtils;
import cn.org.faster.framework.web.exception.model.BasisErrorCode;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import cn.org.faster.framework.web.utils.ResponseBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * @author zhangbowen
 */
@ControllerAdvice
@ResponseBody
@Configuration
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object handleException(MethodArgumentNotValidException exception) {
        return ResponseBuilder.badArgument(BindingResultErrorUtils.resolveErrorMessage(exception.getBindingResult()));
    }

    @ExceptionHandler(BindException.class)
    public Object handleException(BindException exception) {
        return ResponseBuilder.badArgument(BindingResultErrorUtils.resolveErrorMessage(exception.getBindingResult()));
    }

    @ExceptionHandler(TokenValidException.class)
    public Object handleException(TokenValidException exception) {
        return ResponseErrorEntity.error(exception.getErrorCode(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SQLException.class)
    public Object handleException(SQLException exception) {
        exception.printStackTrace();
        return ResponseErrorEntity.error(BasisErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
