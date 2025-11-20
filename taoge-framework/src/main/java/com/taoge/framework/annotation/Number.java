package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by xuejingtao
 * 数字
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Number {
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

    /**
     * 是否为整数
     *
     * @return
     */
    boolean isInteger() default false;
}
