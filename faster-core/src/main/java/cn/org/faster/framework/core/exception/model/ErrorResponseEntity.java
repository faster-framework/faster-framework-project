package cn.org.faster.framework.core.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by zhangbowen
 */
@SuppressWarnings("unchecked")
public class ErrorResponseEntity extends ResponseEntity {
    private ErrorResponseEntity(HttpStatus statusCode) {
        super(statusCode);
    }

    private ErrorResponseEntity(ErrorCode body, HttpStatus status) {
        super(new ResultError(body, status), status);
    }

    private ErrorResponseEntity(ResultError resultError, HttpStatus status) {
        super(resultError, status);
    }

    public static ErrorResponseEntity error(ErrorCode body, HttpStatus status) {
        return new ErrorResponseEntity(body, status);
    }

    public static ErrorResponseEntity error(ResultError resultError, HttpStatus status) {
        return new ErrorResponseEntity(resultError, status);
    }
}
