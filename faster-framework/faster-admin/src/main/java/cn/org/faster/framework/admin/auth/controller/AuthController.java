package cn.org.faster.framework.admin.auth.controller;

import cn.org.faster.framework.admin.auth.model.LoginReq;
import cn.org.faster.framework.admin.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangbowen
 */
@RestController
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * 登录
     *
     * @param loginReq 登录实体
     * @return 登录成功实体
     */
    @PostMapping("/login")
    public ResponseEntity login(@Validated @RequestBody LoginReq loginReq) {
        return authService.login(loginReq);
    }

    /**
     * @return 获取用户信息
     */
    @GetMapping("/userInfo")
    public ResponseEntity userInfo() {
        return ResponseEntity.ok(authService.userInfo());
    }

    /**
     * 退出登录
     *
     * @return httpResponse
     */
    @DeleteMapping("/logout")
    public ResponseEntity logout() {
        authService.logout();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
