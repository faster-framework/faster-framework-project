package cn.org.faster.framework.grpc.annotation;

import io.grpc.MethodDescriptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于调用远程服务双方约定方法
 *
 * @author zhangbowen
 * @since 2019/1/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface GrpcMethod {

    /**
     * @return 方法名，客户端与服务端需统一，默认为反射出的方法名称
     */
    String value() default "";

    /**
     * @return 通信类型，默认UNARY（一条请求一条响应，grpc提供了多种方式）
     */
    MethodDescriptor.MethodType type() default MethodDescriptor.MethodType.UNARY;

}
