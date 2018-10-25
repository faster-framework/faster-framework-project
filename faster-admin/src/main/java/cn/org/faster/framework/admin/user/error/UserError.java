package cn.org.faster.framework.admin.user.error;

import cn.org.faster.framework.web.exception.model.ErrorCode;
import lombok.AllArgsConstructor;

/**
 * @author zhangbowen
 */
@AllArgsConstructor
public enum UserError implements ErrorCode {
    USER_EXIST(1130, "用户已存在"),
    USER_NOT_EXIST(1131, "用户不存在"),
    OLD_PASSWORD_ERROR(1132, "旧密码输入错误"),
    ADMIN_CANNOT_DELETE(1133, "超级管理员用户不可删除"),
    ;

    private int value;
    private String description;

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
