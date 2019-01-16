package cn.org.faster.framework.grpc.core.model;

import io.grpc.MethodDescriptor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author zhangbowen
 * @since 2019/1/14
 */
@Data
public class MethodCallProperty {
    private String methodName;
    private MethodDescriptor.MethodType methodType;
    private String scheme;
    private Method method;
}
