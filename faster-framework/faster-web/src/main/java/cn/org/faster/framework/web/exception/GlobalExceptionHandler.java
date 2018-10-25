package cn.org.faster.framework.web.exception;

import cn.org.faster.framework.web.exception.model.BasisErrorCode;
import cn.org.faster.framework.web.exception.model.ErrorResponseEntity;
import cn.org.faster.framework.web.exception.model.ResultError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
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
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object handleException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        ResultError resultMsg = new ResultError(BasisErrorCode.VALIDATION_FAILED.getValue(), fieldError.getField() + fieldError.getDefaultMessage());
        return ErrorResponseEntity.error(resultMsg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public Object handleException(BindException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        ResultError resultMsg = new ResultError(BasisErrorCode.VALIDATION_FAILED.getValue(), fieldError.getField() + fieldError.getDefaultMessage());
        return ErrorResponseEntity.error(resultMsg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenValidException.class)
    public Object handleException(TokenValidException exception) {
        return ErrorResponseEntity.error(exception.getErrorCode(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SQLException.class)
    public Object handleException(SQLException exception) {
        exception.printStackTrace();
        return ErrorResponseEntity.error(BasisErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
