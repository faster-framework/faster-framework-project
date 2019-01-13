package cn.org.faster.framework.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangbowen
 * @since 2018/10/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RedisListener {
    /**
     * 定义要监听的channel数组，可为pattern模式
     *
     * @return 监听的channel数组
     */
    String[] value() default {};
}
