package cn.org.faster.framework.admin.auth.service;

import cn.org.faster.framework.admin.auth.error.AuthError;
import cn.org.faster.framework.admin.auth.model.LoginReq;
import cn.org.faster.framework.admin.auth.model.LoginRes;
import cn.org.faster.framework.admin.user.entity.SysUser;
import cn.org.faster.framework.admin.user.service.SysUserService;
import cn.org.faster.framework.core.utils.Utils;
import cn.org.faster.framework.web.captcha.service.ICaptchaService;
import cn.org.faster.framework.web.exception.model.ErrorResponseEntity;
import cn.org.faster.framework.web.jwt.service.JwtService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zhangbowen
 */
@Service
public class AuthService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthorizingRealm authorizingRealm;
    @Autowired
    private ICaptchaService captchaService;


    /**
     * 登录
     *
     * @param loginReq loginReq
     * @return ResponseEntity
     */
    public ResponseEntity login(LoginReq loginReq) {
        //验证图形验证码是否正确
        if (!captchaService.valid(loginReq.getCaptcha(), loginReq.getCaptchaToken())) {
            return ErrorResponseEntity.error(AuthError.CAPTCHA_ERROR, HttpStatus.NOT_FOUND);
        }
        SysUser existUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount, loginReq.getAccount()));
        if (existUser == null) {
            return ErrorResponseEntity.error(AuthError.USER_NOT_EXIST, HttpStatus.NOT_FOUND);
        }
        if (!existUser.getPassword().equals(Utils.md5(loginReq.getPassword()))) {
            return ErrorResponseEntity.error(AuthError.PASSWORD_ERROR, HttpStatus.NOT_FOUND);
        }
        String token = jwtService.createToken(existUser.getId(), 0);
        Subject subject = SecurityUtils.getSubject();
        subject.login(new AuthenticationToken() {
            @Override
            public Object getPrincipal() {
                return token;
            }

            @Override
            public Object getCredentials() {
                return token;
            }
        });
        LoginRes loginRes = new LoginRes();
        loginRes.setToken(token);
        loginRes.setName(existUser.getName());
        return ResponseEntity.ok(loginRes);
    }

    /**
     * 退出登录
     */
    public void logout() {
        PrincipalCollection principalCollection = SecurityUtils.getSubject().getPrincipals();
        if (principalCollection != null) {
            authorizingRealm.getAuthorizationCache().remove(principalCollection);
        }
    }

    /**
     * @return 获取当前用户所有权限code
     */
    public Collection<String> permissions() {
        Cache<Object, AuthorizationInfo> cache = authorizingRealm.getAuthorizationCache();
        AuthorizationInfo authorizationInfo = cache.get(SecurityUtils.getSubject().getPrincipals());
        //说明没有缓存，刷新缓存
        if (authorizationInfo == null) {
            SecurityUtils.getSubject().isPermitted("permissions");
            authorizationInfo = cache.get(SecurityUtils.getSubject().getPrincipals());
        }
        return authorizationInfo == null ? new ArrayList<>() : authorizationInfo.getStringPermissions();
    }
}
