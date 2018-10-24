package cn.org.faster.framework.core.web.inteceptor;

import cn.org.faster.framework.core.utils.NetworkUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangbowen
 */
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String method = httpServletRequest.getMethod();
        String queryString = httpServletRequest.getQueryString();
        log.info("访问者ip:{}:{}\n{}:{}{}",
                NetworkUtil.getIp(httpServletRequest),
                httpServletRequest.getRemotePort(),
                method,
                httpServletRequest.getRequestURI(),
                StringUtils.isEmpty(queryString) ? "" : "?".concat(queryString)
        );
        return true;
    }
}
