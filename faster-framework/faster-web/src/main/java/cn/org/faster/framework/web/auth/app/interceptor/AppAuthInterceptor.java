package cn.org.faster.framework.web.auth.app.interceptor;

import cn.org.faster.framework.core.cache.context.CacheFacade;
import cn.org.faster.framework.web.auth.app.annotation.Login;
import cn.org.faster.framework.web.auth.app.service.AuthService;
import cn.org.faster.framework.web.context.model.RequestContext;
import cn.org.faster.framework.web.context.model.SpringAppContextFacade;
import cn.org.faster.framework.web.context.model.WebContextFacade;
import cn.org.faster.framework.web.exception.TokenValidException;
import cn.org.faster.framework.web.exception.model.BasicErrorCode;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangbowen
 */
public class AppAuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            boolean hasMethodLogin = handlerMethod.hasMethodAnnotation(Login.class);
            Login loginClass = handlerMethod.getBeanType().getAnnotation(Login.class);
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            RequestContext requestContext = WebContextFacade.getRequestContext();
            //解码,设置appAuthContext
            if (!StringUtils.isEmpty(jwtToken) && jwtToken.length() > 7) {
                AuthService authService = SpringAppContextFacade.applicationContext.getBean(AuthService.class);
                Claims claims = authService.parseToken(jwtToken);
                if (claims != null) {
                    String userId = claims.getAudience();
                    //如果当前不允许多端登录，验证缓存中的token是否与当前token相等。
                    //如果为多端登录，则不需要验证缓存,只需要验证秘钥是否正确即可。
                    if (!authService.isMultipartTerminal()) {
                        String cacheToken = CacheFacade.get(AuthService.AUTH_TOKEN_PREFIX + userId);
                        if (!StringUtils.isEmpty(cacheToken) && jwtToken.equals(cacheToken)) {
                            requestContext.setUserId(Long.parseLong(userId));
                        }
                    } else {
                        requestContext.setUserId(Long.parseLong(userId));
                    }
                }

            }
            //如果需要验证，验证appAuth是否为null
            if ((hasMethodLogin || loginClass != null) && requestContext.getUserId() == null) {
                throw new TokenValidException(BasicErrorCode.TOKEN_INVALID);
            } else {
                //放入ThreadLocal中
                WebContextFacade.setRequestContext(requestContext);
            }
        }
        return true;
    }
}
