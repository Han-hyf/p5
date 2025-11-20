package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * 验证身份证
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface IdCard {
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
