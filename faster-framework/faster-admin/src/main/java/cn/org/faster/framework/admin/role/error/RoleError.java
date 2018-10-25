package cn.org.faster.framework.admin.role.error;

import cn.org.faster.framework.web.exception.model.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangbowen
 */
@AllArgsConstructor
@Getter
public enum RoleError implements ErrorCode {
    ADMIN_CANNOT_DELETE(1120, "超级管理员角色不可删除");


    private int value;
    private String description;
}
