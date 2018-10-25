package cn.org.faster.framework.sms.modules.sms.service;

/**
 * @author zhangbowen
 * @since 2018/9/3
 */
public interface ISmsService<T> {
    boolean send(T param);
}
