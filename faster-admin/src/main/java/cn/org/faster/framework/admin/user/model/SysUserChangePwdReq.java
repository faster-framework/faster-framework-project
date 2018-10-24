package cn.org.faster.framework.admin.user.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangbowen
 */
@Data
public class SysUserChangePwdReq {
    @NotBlank(message = "请输入旧密码")
    private String oldPwd;
    @NotBlank(message = "请输入新密码")
    private String password;
}
