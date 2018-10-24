package cn.org.faster.framework.core.web.context;

import lombok.Data;

/**
 * @author zhangbowen
 * @since 2018/8/27
 */
@Data
public class RequestContextConcrete implements RequestContext{
    private String ip;
    private String uri;
    private Long userId;
}
