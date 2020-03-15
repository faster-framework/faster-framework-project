package cn.org.faster.framework.web.exception.handler;

import cn.org.faster.framework.web.exception.model.ResponseErrorEntity;

/**
 * @author zhangbowen
 * @since 2020/3/14
 */
public interface ResponseErrorEntityExecutor {
    ResponseErrorEntity execute(Exception ex);
}
