package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by xuejingtao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Size {
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

    /**
     * 最小值
     * @return
     */
    int min() default 0;

    /**
     * 最大值
     * @return
     */
    int max();
}
