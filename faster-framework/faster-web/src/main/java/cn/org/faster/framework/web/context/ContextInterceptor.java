package cn.org.faster.framework.web.context;

import cn.org.faster.framework.web.context.model.RequestContext;
import cn.org.faster.framework.web.context.model.WebContextFacade;
import cn.org.faster.framework.web.utils.NetworkUtil;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zhangbowen
 */
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        setRequestContext(request);
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断当前请求是否为接口
        boolean hasResponseBody = method.isAnnotationPresent(ResponseBody.class);
        boolean hasRestController = handlerMethod.getBeanType().isAnnotationPresent(RestController.class);
        RequestContext requestContext = WebContextFacade.getRequestContext();
        requestContext.setApiRequest(hasResponseBody || hasRestController);
        WebContextFacade.setRequestContext(requestContext);
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
