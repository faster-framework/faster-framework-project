package cn.org.faster.framework.web.context.model;

import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * @author zhangbowen
 */
public interface RequestContext {
    /**
     * 请求ip
     * @return ip地址
     */
    String getIp();
    void setIp(String ip);

    /**
     * 请求路径
     * @return 不携带参数
     */
    String getUri();
    void setUri(String uri);

    /**
     * 用户id
     * @return 引入auth模块时自动注入
     */
    Long getUserId();
    void setUserId(Long userId);

    /**
     * 是否为api请求
     * @return true/false
     */
    boolean isApiRequest();
    void setApiRequest(boolean apiRequest);

    /**
     * 扩展字段map格式
     *
     * @return map
     */
    Map<String, Object> getExtraMap();

    void setExtraMap(Map<String, Object> map);

    /**
     * 设置扩展字段Map内容
     * @param key 键
     * @param value 值
     */
    void extraMap(String key, Object value);

    /**
     * 获取扩展字段Map值
     * @param key 键
     * @return 值
     */
    Object extraMap(String key);

    /**
     * 获取扩展字段map值，如果不存在返回默认值
     * @param key 键
     * @param defaultValue 默认值
     * @return 值
     */
    Object extraMapOrDefault(String key, Object defaultValue);


    /**
     * 扩展字段bean格式
     *
     * @return 泛型
     */
    <T> T getExtraBean(TypeReference<T> beanType);
    void setExtraBean(Object bean);


}
