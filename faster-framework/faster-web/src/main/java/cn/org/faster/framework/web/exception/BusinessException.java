package cn.org.faster.framework.web.exception;


import cn.org.faster.framework.web.exception.model.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;

    public BusinessException() {
        super();
    }

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }


    public BusinessException(Integer code, String description) {
        this.errorCode = new ErrorCode() {
            @Override
            public int getValue() {
                return code;
            }

            @Override
            public String getDescription() {
                return description;
            }
        };
    }


    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    public static BusinessException build(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }

    public static BusinessException build(ErrorCode errorCode, String msg) {
        return new BusinessException(errorCode.getValue(), msg);
    }
    public static BusinessException build(Integer code, String msg) {
        return new BusinessException(code, msg);
    }
}
