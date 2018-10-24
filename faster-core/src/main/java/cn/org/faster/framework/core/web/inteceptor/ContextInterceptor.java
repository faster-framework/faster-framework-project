package cn.org.faster.framework.core.web.inteceptor;

import cn.org.faster.framework.core.utils.NetworkUtil;
import cn.org.faster.framework.core.web.context.RequestContext;
import cn.org.faster.framework.core.web.context.WebContextFacade;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangbowen
 */
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        setRequestContext(request);
        return true;
    }

    private void setRequestContext(HttpServletRequest request) {
        RequestContext requestContext = WebContextFacade.getRequestContext();
        requestContext.setIp(NetworkUtil.getIp(request));
        requestContext.setUri(request.getRequestURI());
        WebContextFacade.setRequestContext(requestContext);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
        WebContextFacade.removeRequestContext();
    }
}
