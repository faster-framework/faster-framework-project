package cn.org.faster.framework.core.web.context;

/**
 * 获取web请求声明周期的全局变量
 *
 * @author zhangbowen 2018/5/24 17:08
 */
public class WebContextFacade {
    private static ThreadLocal<RequestContext> requestContextThreadLocal = new ThreadLocal<>();

    public static RequestContext getRequestContext() {
        RequestContext requestContext = requestContextThreadLocal.get();
        return requestContext == null ? new RequestContextConcrete() : requestContext;
    }

    public static void setRequestContext(RequestContext requestContext) {
        requestContextThreadLocal.set(requestContext);
    }

    public static void removeRequestContext() {
        requestContextThreadLocal.remove();
    }
}
