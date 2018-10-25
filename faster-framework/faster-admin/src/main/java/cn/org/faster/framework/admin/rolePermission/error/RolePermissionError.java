package cn.org.faster.framework.admin.rolePermission.error;

import cn.org.faster.framework.web.exception.model.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangbowen
 */
@AllArgsConstructor
@Getter
public enum RolePermissionError implements ErrorCode {
    CANNOT_CHOOSE_ADMIN_PERMISSION(1140, "不可为超级管理员选择权限");

    private int value;
    private String description;
}
