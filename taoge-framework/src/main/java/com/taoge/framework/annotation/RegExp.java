package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * 正则校验注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface RegExp {
    /**
     * 错误提示属性名
     */
    String name() default "";

    /**
     * 错误提示消息
     */
    String errorMsg() default "";

    /**
     * 正则表达式
     */
    String pattern();
}
