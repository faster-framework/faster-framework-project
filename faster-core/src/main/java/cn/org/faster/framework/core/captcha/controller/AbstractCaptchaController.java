package cn.org.faster.framework.core.captcha.controller;

import cn.org.faster.framework.core.captcha.service.ICaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author zhangbowen
 */
public abstract class AbstractCaptchaController {
    @Autowired
    protected ICaptchaService captchaService;


    /**
     * 获取图形验证码
     *
     * @return httpResponse
     */
    @GetMapping("/captcha")
    public ResponseEntity captcha() {
        return ResponseEntity.ok(captchaService.generateCaptcha());
    }
}
