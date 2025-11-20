package com.taoge.framework.annotation;


import java.lang.annotation.*;

/**
 * 枚举值范围校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Range {

    /**
     * 错误提示属性
     * @return
     */
    Class<?> clazz();

    /**
     * 错误提示消息
     * @return
     */
    String errorMsg() default "";


    String key() default "";

}
