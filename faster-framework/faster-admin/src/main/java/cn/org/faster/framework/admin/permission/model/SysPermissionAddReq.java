package cn.org.faster.framework.admin.permission.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhangbowen
 */
@Data
public class SysPermissionAddReq {
    @NotBlank(message = "请输入权限名称")
    private String name;
    @NotBlank(message = "请输入权限编码")
    private String code;
    @NotNull(message = "请选择上级id")
    private Long parentId;
}
