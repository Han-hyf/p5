package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by xuejingtao
 * 非空
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface NotNull {
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
     * 是否不能为空字符串
     *
     * @return
     */
    boolean notEmpty() default true;
}
