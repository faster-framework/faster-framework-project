package cn.org.faster.framework.core.web.context;

/**
 * @author zhangbowen
 */
public interface RequestContext {
    String getIp();

    void setIp(String ip);

    String getUri();

    void setUri(String uri);

    Long getUserId();

    void setUserId(Long userId);
}
