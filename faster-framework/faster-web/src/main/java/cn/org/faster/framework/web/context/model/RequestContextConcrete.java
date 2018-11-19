package cn.org.faster.framework.web.context.model;

import lombok.Data;

/**
 * @author zhangbowen
 * @since 2018/8/27
 */
@Data
public class RequestContextConcrete implements RequestContext {
    private String ip;
    private String uri;
    private Long userId;
}
