package cn.org.faster.framework.mybatis.spring.boot.autoconfigure;

import cn.org.faster.framework.web.exception.model.BasisErrorCode;
import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangbowen
 * @since 2018/10/25
 */
@ControllerAdvice
public class MyBatisExceptionHandler {
    @ResponseBody
    @ExceptionHandler(PersistenceException.class)
    public Object handleException(PersistenceException exception) {
        exception.printStackTrace();
        return ResponseErrorEntity.error(BasisErrorCode.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
