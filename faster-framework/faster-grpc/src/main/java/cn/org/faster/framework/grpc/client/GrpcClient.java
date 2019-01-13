package cn.org.faster.framework.grpc.client;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * grpc客户端组件。被{@link cn.org.faster.framework.grpc.GrpcScan}扫描注入到spring中
 * @author zhangbowen
 * @since 2019/1/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GrpcClient {
    /**
     * @return 远程服务名，与faster.grpc.cn.org.faster.framework.grpc.client.service中的服务名需对应
     */
    String value();
}
