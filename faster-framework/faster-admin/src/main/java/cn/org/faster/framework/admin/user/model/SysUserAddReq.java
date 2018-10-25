package cn.org.faster.framework.admin.user.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangbowen
 */
@Data
public class SysUserAddReq {
    @NotBlank(message = "请输入账号")
    private String account;
    @NotBlank(message = "请输入密码")
    private String password;
    @NotBlank(message = "请输入姓名")
    private String name;
}
