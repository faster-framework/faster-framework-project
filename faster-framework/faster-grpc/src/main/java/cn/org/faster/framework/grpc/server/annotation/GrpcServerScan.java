package cn.org.faster.framework.grpc.server.annotation;

import cn.org.faster.framework.grpc.server.register.GrpcApiRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 将扫描到的grpc组件注入到spring中，扫描client与server组件
 *
 * @author zhangbowen
 * @since 2019/1/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(GrpcApiRegister.class)
public @interface GrpcServerScan {
    /**
     * @return 扫描的包
     */
    String[] basePackages() default {};
}
