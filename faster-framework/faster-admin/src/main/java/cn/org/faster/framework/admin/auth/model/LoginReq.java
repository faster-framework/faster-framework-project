package cn.org.faster.framework.admin.auth.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangbowen
 */
@Data
public class LoginReq {
    @NotBlank(message = "请填写用户名")
    private String account;
    @NotBlank(message = "请填写密码")
    private String password;
    @NotBlank(message = "请填写验证码")
    private String captcha;
    @NotBlank(message = "图形验证码token错误")
    private String captchaToken;

}
