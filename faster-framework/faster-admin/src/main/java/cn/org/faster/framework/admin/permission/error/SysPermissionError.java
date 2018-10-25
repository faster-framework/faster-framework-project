package cn.org.faster.framework.admin.permission.error;

import cn.org.faster.framework.web.exception.model.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhangbowen
 */
@AllArgsConstructor
@Getter
public enum SysPermissionError implements ErrorCode {
    PERMISSION_PARENT_NOT_EXIST(1110, "权限父级不存在"),
    PERMISSION_CODE_EXIST(1111, "权限编码已经存在");
    private int value;
    private String description;
}
