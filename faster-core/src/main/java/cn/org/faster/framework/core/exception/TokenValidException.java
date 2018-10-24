package cn.org.faster.framework.core.exception;


import cn.org.faster.framework.core.exception.model.ErrorCode;

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
