package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * 验证密码，按密码要求更改
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Password {

    /**
     * 错误提示属性名
     *
     * @return
     */
    String name() default "";

    /**
     * 错误提示消息
     *
     * @return
     */
    String errorMsg() default "";
}
