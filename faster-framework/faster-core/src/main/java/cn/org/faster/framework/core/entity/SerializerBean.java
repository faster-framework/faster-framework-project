package cn.org.faster.framework.core.entity;

import com.fasterxml.jackson.databind.JavaType;
import lombok.Data;

/**
 * @author zhangbowen
 * @since 2019/1/9
 */
@Data
public class SerializerBean {
    private JavaType type;
    private String json;
}
