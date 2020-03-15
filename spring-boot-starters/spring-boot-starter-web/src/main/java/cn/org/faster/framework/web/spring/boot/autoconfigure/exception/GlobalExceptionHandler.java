package cn.org.faster.framework.web.spring.boot.autoconfigure.exception;

import cn.org.faster.framework.core.utils.error.BindingResultErrorUtils;
import cn.org.faster.framework.web.exception.BusinessException;
import cn.org.faster.framework.web.exception.TokenValidException;
import cn.org.faster.framework.web.exception.handler.ExceptionExecutorHandler;
import cn.org.faster.framework.web.exception.handler.ResponseErrorEntityExecutor;
import cn.org.faster.framework.web.exception.model.BasicErrorCode;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import cn.org.faster.framework.web.exception.model.ResultError;
import cn.org.faster.framework.web.secret.HttpMessageDecryptException;
import cn.org.faster.framework.web.version.ApiVersionDiscardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

/**
 * @author zhangbowen
 */
@ControllerAdvice
@Configuration
public class GlobalExceptionHandler implements ResponseErrorEntityExecutor {
    @Autowired
    private ExceptionExecutorHandler exceptionExecutorHandler;

    /**
     * 接口异常返回json信息
     *
     * @param exception 异常
     * @return ResponseErrorEntity
     */
    public ResponseErrorEntity execute(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException) {
            //参数绑定异常拦截
            return ResponseErrorEntity.error(
                    BusinessException.build(BasicErrorCode.PARAM_ERROR,
                            BindingResultErrorUtils.resolveErrorMessage(
                                    ((MethodArgumentNotValidException) exception).getBindingResult()))
                            .getErrorCode()
                    , HttpStatus.BAD_REQUEST);
        } else if (exception instanceof BindException) {
            //参数绑定异常拦截
            return ResponseErrorEntity.error(BusinessException.build(BasicErrorCode.PARAM_ERROR,
                    BindingResultErrorUtils.resolveErrorMessage(((BindException) exception).getBindingResult())).getErrorCode(), HttpStatus.BAD_REQUEST);
        } else if (exception instanceof TokenValidException) {
            //token失效异常拦截
            return ResponseErrorEntity.error(((TokenValidException) exception).getErrorCode(), HttpStatus.UNAUTHORIZED);
        } else if (exception instanceof SQLException) {
            //sql异常
            exception.printStackTrace();
            return ResponseErrorEntity.error(BasicErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (exception instanceof ApiVersionDiscardException) {
            //版本废弃异常拦截
            ResultError resultMsg = new ResultError(BasicErrorCode.DISCARD_ERROR.getValue(), exception.getMessage());
            return ResponseErrorEntity.error(resultMsg, HttpStatus.BAD_REQUEST);
        } else if (exception instanceof HttpMessageDecryptException) {
            //加密接口解密失败异常
            ResultError resultMsg = new ResultError(BasicErrorCode.PARAM_ERROR.getValue(), exception.getMessage());
            return ResponseErrorEntity.error(resultMsg, HttpStatus.BAD_REQUEST);
        } else if (exception instanceof BusinessException) {
            return ResponseErrorEntity.error(((BusinessException) exception).getErrorCode(), HttpStatus.BAD_REQUEST);
        } else if (exception instanceof RuntimeException) {
            //运行时异常
            exception.printStackTrace();
            return ResponseErrorEntity.error(BasicErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        exception.printStackTrace();
        return ResponseErrorEntity.error(BasicErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * @param exception 参数绑定异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Object handleException(MethodArgumentNotValidException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    /**
     * @param exception 参数绑定异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(BindException.class)
    public Object handleException(BindException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    /**
     * @param exception token失效异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(TokenValidException.class)
    public Object handleException(TokenValidException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    /**
     * @param exception sql异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(SQLException.class)
    public Object handleException(SQLException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    /**
     * @param exception 版本废弃异常拦截
     * @return 错误信息
     */
    @ExceptionHandler(value = ApiVersionDiscardException.class)
    public Object handleException(ApiVersionDiscardException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    /**
     * @param exception 加密接口解密失败异常
     * @return 错误信息
     */
    @ExceptionHandler(value = HttpMessageDecryptException.class)
    public Object handleException(HttpMessageDecryptException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    /**
     * @param exception 业务异常
     * @return 错误信息
     */
    @ExceptionHandler(BusinessException.class)
    public Object catchBusinessException(BusinessException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    /**
     * @param exception 运行时异常
     * @return 错误信息
     */
    @ExceptionHandler(RuntimeException.class)
    public Object catchRuntimeException(RuntimeException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }
}
