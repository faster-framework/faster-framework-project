package cn.org.faster.framework.auth;

import cn.hutool.extra.servlet.ServletUtil;
import cn.org.faster.framework.auth.annotation.Login;
import cn.org.faster.framework.core.cache.context.CacheFacade;
import cn.org.faster.framework.core.constants.HeaderConstants;
import cn.org.faster.framework.web.context.model.WebContextFacade;
import cn.org.faster.framework.web.exception.TokenValidException;
import cn.org.faster.framework.web.exception.model.BasicErrorCode;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangbowen
 */
public class AuthInterceptor implements HandlerInterceptor {

    private AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean hasMethodLogin = handlerMethod.hasMethodAnnotation(Login.class);
        Login loginClass = handlerMethod.getBeanType().getAnnotation(Login.class);

        //如果方法与类均不存在注解，说明不需要验证登录
        if (!hasMethodLogin && loginClass == null) {
            //置空用户id
            WebContextFacade.getRequestContext().setUserId(null);
            return true;
        }

        String jwtToken = request.getHeader(HeaderConstants.AUTH_TOKEN);
        //如果header中token为空，获取cookie中的token
        if (StringUtils.isEmpty(jwtToken)) {
            Cookie cookie = ServletUtil.getCookie(request, HeaderConstants.AUTH_TOKEN.toLowerCase());
            //如果cookie中的token为空，抛出异常
            if (cookie == null) {
                throw new TokenValidException(BasicErrorCode.TOKEN_INVALID);
            }
            jwtToken = cookie.getValue();
        }
        //如果jwt位数无效，抛出异常
        if (jwtToken.length() <= 7) {
            throw new TokenValidException(BasicErrorCode.TOKEN_INVALID);
        }
        Claims claims = authService.parseToken(jwtToken);
        //解码失败，抛出异常
        if (claims == null) {
            throw new TokenValidException(BasicErrorCode.TOKEN_INVALID);
        }
        String userId = claims.getAudience();
        //如果不允许多端同时在线，需要验证是否与缓存中的token一致
        if (!authService.isMultipartTerminal()) {
            String cacheToken = CacheFacade.get(AuthService.AUTH_TOKEN_PREFIX + userId);
            //如果不一致，抛出异常
            if (!jwtToken.equals(cacheToken)) {
                throw new TokenValidException(BasicErrorCode.TOKEN_INVALID);
            }
        }
        //如果允许多端同时在线，放行
        //设置当前用户id
        WebContextFacade.getRequestContext().setUserId(Long.parseLong(userId));
        return true;
    }
}
