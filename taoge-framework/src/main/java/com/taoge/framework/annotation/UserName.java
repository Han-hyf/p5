package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * 用户名校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface UserName {
    /**
     * 错误提示属性名
     * @return
     */
    String name() default "";

    /**
     * 错误提示消息
     * @return
     */
    String errorMsg() default "";
}
