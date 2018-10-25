package cn.org.faster.framework.redis.annotation;

import java.lang.annotation.*;

/**
 * @author zhangbowen
 * @since 2018/10/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RedisListenerScan {
    /**
     * @return 扫描的包
     */
    String[] basePackages() default {};

    /**
     * @return 扫描的注解
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;
}
