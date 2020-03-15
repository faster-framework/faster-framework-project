package cn.org.faster.framework.web.context.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;

import java.util.Map;

/**
 * @author zhangbowen
 * @since 2018/8/27
 */
@Data
public class RequestContextConcrete implements RequestContext {
    private String ip;
    private String uri;
    private Long userId;
    private boolean apiRequest;

    /**
     * 扩展字段Map类型
     */
    private Map<String, Object> extraMap;

    /**
     * 设置扩展字段Map内容
     *
     * @param key   键
     * @param value 值
     */
    public void extraMap(String key, Object value) {
        this.extraMap.put(key, value);
    }

    /**
     * 获取扩展字段Map值
     *
     * @param key 键
     * @return 值
     */
    public Object extraMap(String key) {
        return this.extraMap.get(key);
    }

    /**
     * 获取扩展字段map值，如果不存在返回默认值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public Object extraMapOrDefault(String key, Object defaultValue) {
        return this.extraMap.getOrDefault(key, defaultValue);
    }

    /**
     * 扩展字段，json字符串
     */
    private String extraJson;

    @Override
    public void setExtraBean(Object bean) {
        this.extraJson = JSON.toJSONString(bean);
    }

    @Override
    public <T> T getExtraBean(TypeReference<T> beanType) {
        return JSON.parseObject(this.extraJson, beanType);
    }
}
