package cn.org.faster.framework.admin.role.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangbowen
 */
@Data
public class SysRoleReq {
    @NotBlank(message = "请输入角色名称")
    private String name;
}
