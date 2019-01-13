package cn.org.faster.framework.grpc;

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
@Import(GrpcScannerRegister.class)
public @interface GrpcScan {
    /**
     * @return 扫描的包
     */
    String[] basePackages() default {};
}
