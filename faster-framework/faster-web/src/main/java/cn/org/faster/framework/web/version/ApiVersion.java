package cn.org.faster.framework.web.version;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangbowen
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiVersion {
    /**
     * @return 版本号
     */
    int value() default 0;

    /**
     * @return 是否废弃
     */
    boolean discard() default false;

    /**
     * @return 是否覆盖废弃
     */
    boolean overrideDiscard() default false;
}
