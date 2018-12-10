package cn.org.faster.framework.web.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 返回错误实体类
 *
 * @author zhangbowen
 */
public class ResponseErrorEntity extends ResponseEntity<ResultError> {
    private ResponseErrorEntity(HttpStatus statusCode) {
        super(statusCode);
    }

    private ResponseErrorEntity(ErrorCode body, HttpStatus status) {
        super(new ResultError(body, status), status);
    }

    private ResponseErrorEntity(ResultError resultError, HttpStatus status) {
        super(resultError, status);
    }

    public static ResponseErrorEntity error(ErrorCode body, HttpStatus status) {
        return new ResponseErrorEntity(body, status);
    }

    public static ResponseErrorEntity error(ResultError resultError, HttpStatus status) {
        return new ResponseErrorEntity(resultError, status);
    }
}
