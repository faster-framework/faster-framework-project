package cn.org.faster.framework.web.exception.model;

/**
 * @author zhangbowen
 */
public enum BasicErrorCode implements ErrorCode {
    ERROR(1000, "处理失败"),
    PARAM_ERROR(1001, "参数异常"),
    TOKEN_INVALID(1002, "登录状态过期"),
    SMS_SEND_ERROR(1003, "短信发送失败"),
    DISCARD_ERROR(1005, "版本过时"),
    PERMISSION_ERROR(1006, "权限不足"),
    SERVER_ERROR(1007, "服务器正在维护"),
    ;

    private int value;
    private String description;

    BasicErrorCode() {
    }

    BasicErrorCode(int value, String description) {
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
