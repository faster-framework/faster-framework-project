package cn.org.faster.framework.web.spring.boot.autoconfigure.exception;

import cn.org.faster.framework.core.utils.error.BindingResultErrorUtils;
import cn.org.faster.framework.web.exception.BusinessException;
import cn.org.faster.framework.web.exception.TokenValidException;
import cn.org.faster.framework.web.exception.model.BasicErrorCode;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import cn.org.faster.framework.web.exception.model.ResultError;
import cn.org.faster.framework.web.secret.HttpMessageDecryptException;
import cn.org.faster.framework.web.version.ApiVersionDiscardException;
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

    /**
     * @param exception 参数绑定异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object handleException(MethodArgumentNotValidException exception) {
        return ResponseErrorEntity.error(BusinessException.build(BasicErrorCode.PARAM_ERROR, BindingResultErrorUtils.resolveErrorMessage(exception.getBindingResult())).getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    /**
     * @param exception 参数绑定异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(BindException.class)
    public Object handleException(BindException exception) {
        return ResponseErrorEntity.error(BusinessException.build(BasicErrorCode.PARAM_ERROR, BindingResultErrorUtils.resolveErrorMessage(exception.getBindingResult())).getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    /**
     * @param exception token失效异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(TokenValidException.class)
    public Object handleException(TokenValidException exception) {
        return ResponseErrorEntity.error(exception.getErrorCode(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * @param exception sql异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(SQLException.class)
    public Object handleException(SQLException exception) {
        exception.printStackTrace();
        return ResponseErrorEntity.error(BasicErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param exception 版本废弃异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(value = ApiVersionDiscardException.class)
    public Object handleException(ApiVersionDiscardException exception) {
        ResultError resultMsg = new ResultError(BasicErrorCode.DISCARD_ERROR.getValue(), exception.getMessage());
        return ResponseErrorEntity.error(resultMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param exception 加密接口解密失败异常
     * @return 错误信息
     */
    @ExceptionHandler(value = HttpMessageDecryptException.class)
    public Object handleException(HttpMessageDecryptException exception) {
        ResultError resultMsg = new ResultError(BasicErrorCode.PARAM_ERROR.getValue(), exception.getMessage());
        return ResponseErrorEntity.error(resultMsg, HttpStatus.BAD_REQUEST);
    }

    /**
     * RuntimeException 异常的拦截
     */
    @ExceptionHandler(RuntimeException.class)
    public Object catchRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return ResponseErrorEntity.error(BasicErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 业务异常
     *
     * @param e 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Object catchBusinessException(BusinessException e) {
        return ResponseErrorEntity.error(e.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

}
