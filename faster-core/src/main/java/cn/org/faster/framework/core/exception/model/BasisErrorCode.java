package cn.org.faster.framework.core.exception.model;

/**
 * @author zhangbowen
 */
public enum BasisErrorCode implements ErrorCode {
    SERVER_ERROR(1000, "服务器正在维护"),
    VALIDATION_FAILED(1001, "参数异常"),
    TOKEN_INVALID(1002, "登录状态过期"),
    SMS_SEND_ERROR(1003, "短信发送失败"),
    ;

    private int value;
    private String description;

    BasisErrorCode() {
    }

    BasisErrorCode(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
