package cn.org.faster.framework.web.captcha.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhangbowen
 */
@Data
@AllArgsConstructor
public class CaptchaBean {
    private String token;
    private String img;
}
