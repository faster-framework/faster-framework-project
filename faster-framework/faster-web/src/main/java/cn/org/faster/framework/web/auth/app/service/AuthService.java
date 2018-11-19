package cn.org.faster.framework.web.auth.app.service;

import cn.org.faster.framework.core.cache.context.CacheFacade;
import cn.org.faster.framework.web.jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangbowen
 */
@Data
public class AuthService {
    public static final String AUTH_TOKEN_PREFIX = "auth-token:";
    private boolean multipartTerminal;
    @Autowired
    private JwtService jwtService;

    /**
     * 解密，使用项目配置秘钥
     *
     * @param token 要解密的token
     * @return Claims
     */
    public Claims parseToken(String token) {
        return jwtService.parseToken(token);
    }

    /**
     * 生成token,使用项目配置秘钥，存入缓存
     *
     * @param audience  接收者
     * @param expSecond 过期时间(秒)
     * @return String
     */
    public String createToken(Object audience, long expSecond) {
        String token = jwtService.createToken(audience, expSecond);
        //如果不允许多端登录，设置token到缓存中
        if (!multipartTerminal) {
            CacheFacade.set(AUTH_TOKEN_PREFIX + audience.toString(), token, expSecond);
        }
        return token;
    }
}
