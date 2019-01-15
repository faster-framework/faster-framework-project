package cn.org.faster.framework.grpc.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * grpc客户端组件。被{@link GrpcClientScan}扫描注入到spring中。
 * 用于调用远程服务
 *
 * @author zhangbowen
 * @since 2019/1/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GrpcService {

    /**
     * @return 远程服务名，与faster.grpc.cn.org.faster.framework.grpc.client.service中的服务名需对应
     */
    String value();

    /**
     * @return 通过scheme确定server端的某个服务类，故要server一致。
     */
    String scheme();
}
