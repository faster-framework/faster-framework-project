package cn.org.faster.framework.web.captcha.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangbowen
 */
@Data
public class CaptchaValidReq {
    @NotBlank
    private String captcha;
    @NotBlank
    private String token;
}
