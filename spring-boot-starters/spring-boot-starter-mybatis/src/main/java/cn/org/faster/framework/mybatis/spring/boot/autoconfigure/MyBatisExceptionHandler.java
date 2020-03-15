package cn.org.faster.framework.mybatis.spring.boot.autoconfigure;

import cn.org.faster.framework.web.exception.handler.ExceptionExecutorHandler;
import cn.org.faster.framework.web.exception.handler.ResponseErrorEntityExecutor;
import cn.org.faster.framework.web.exception.model.BasicErrorCode;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author zhangbowen
 * @since 2018/10/25
 */
@ControllerAdvice
public class MyBatisExceptionHandler implements ResponseErrorEntityExecutor {
    @Autowired
    private ExceptionExecutorHandler exceptionExecutorHandler;

    @ExceptionHandler(PersistenceException.class)
    public Object handleException(PersistenceException exception) {
        return exceptionExecutorHandler.exception(exception, this);
    }

    @Override
    public ResponseErrorEntity execute(Exception exception) {
        if (exception instanceof PersistenceException) {
            exception.printStackTrace();
            return ResponseErrorEntity.error(BasicErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseErrorEntity.error(BasicErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
