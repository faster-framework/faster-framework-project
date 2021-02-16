package cn.org.faster.framework.web.captcha.controller;

import cn.org.faster.framework.web.captcha.bean.CaptchaBean;
import cn.org.faster.framework.web.captcha.bean.CaptchaValidReq;
import cn.org.faster.framework.web.captcha.service.ICaptchaService;
import cn.org.faster.framework.web.exception.model.BasicErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    /**
     * 验证图形验证码
     * @param captchaValidReq captchaValidReq
     * @return httpResponse
     */
    @GetMapping("/captcha/valid")
    public ResponseEntity captcha(@Validated CaptchaValidReq captchaValidReq) {
        if(!captchaService.valid(captchaValidReq.getCaptcha(),captchaValidReq.getToken())){
            BasicErrorCode.ERROR.throwException("验证码输入错误");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
