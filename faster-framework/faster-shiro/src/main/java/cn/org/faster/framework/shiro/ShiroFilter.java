package cn.org.faster.framework.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangbowen
 */
public class ShiroFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwtToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return new AuthenticationToken() {
            @Override
            public Object getPrincipal() {
                return jwtToken;
            }

            @Override
            public Object getCredentials() {
                return jwtToken;
            }
        };
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return executeLogin(servletRequest, servletResponse);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        //可能存在不需要权限的接口，前端无须传递token，故登录失败。
        //权限认证交由realm
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE");
        String accessControlRequestHeaders = httpServletRequest.getHeader("Access-Control-Request-Headers");
        if (!StringUtils.isEmpty(accessControlRequestHeaders)) {
            httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, accessControlRequestHeaders);
        }
        httpResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        if (RequestMethod.OPTIONS.name().equals(WebUtils.toHttp(request).getMethod())) {
            httpResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
