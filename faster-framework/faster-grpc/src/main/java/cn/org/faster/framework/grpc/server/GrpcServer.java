package cn.org.faster.framework.grpc.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * grpc服务端组件。被{@link cn.org.faster.framework.grpc.GrpcScan}扫描注入到spring中
 *
 * @author zhangbowen
 * @since 2019/1/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GrpcServer {
    /**
     * @return 提供的服务名称，client端调用
     */
    String value();
}
