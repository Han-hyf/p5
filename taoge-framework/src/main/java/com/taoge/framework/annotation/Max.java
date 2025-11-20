package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by xuejingtao
 * 最大值
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Max {
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
     * 值
     *
     * @return
     */
    long value();
}
