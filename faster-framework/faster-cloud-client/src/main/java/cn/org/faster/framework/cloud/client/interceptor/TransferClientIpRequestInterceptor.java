package cn.org.faster.framework.cloud.client.interceptor;

import cn.org.faster.framework.web.utils.NetworkUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangbowen
 * @since 2019-06-20
 * 转发ip
 */
public class TransferClientIpRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        //放入ip
        template.header(NetworkUtil.HEADER_X_FORWARDED_FOR, NetworkUtil.getIp(request));
    }
}
