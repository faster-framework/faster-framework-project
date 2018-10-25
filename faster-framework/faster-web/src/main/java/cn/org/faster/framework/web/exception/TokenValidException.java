package cn.org.faster.framework.web.exception;


import cn.org.faster.framework.web.exception.model.ErrorCode;

/**
 * @author zhangbowen
 */
public class TokenValidException extends RuntimeException {
    private ErrorCode errorCode;

    public TokenValidException() {
    }

    public TokenValidException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
