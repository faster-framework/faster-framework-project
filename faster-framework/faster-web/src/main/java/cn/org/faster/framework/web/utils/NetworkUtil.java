package cn.org.faster.framework.web.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * @author zhangbowen
 */
public class NetworkUtil {
    public static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    public static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    public static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    public static final String HTTP_X_FORWARD_IP = "HTTP_X_FORWARDED_FOR";


    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request 请求
     * @return ip
     */
    public static String getIp(HttpServletRequest request) {
        try {
            // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
            String ip = request.getHeader(HEADER_X_FORWARDED_FOR);

            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader(PROXY_CLIENT_IP);
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader(WL_PROXY_CLIENT_IP);
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader(HTTP_CLIENT_IP);
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader(HTTP_X_FORWARD_IP);
                }
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
            } else if (ip.length() > 15) {
                String[] ips = ip.split(",");
                for (String ip1 : ips) {
                    if (!("unknown".equalsIgnoreCase(ip1))) {
                        ip = ip1;
                        break;
                    }
                }
            }
            if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
            return ip;
        } catch (Exception e) {
            return "";
        }
    }
}
