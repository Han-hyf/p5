package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by xuejingtao
 * 验证数字，正负数、小数位、整数位
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface Digits {
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
     * 可否是负数
     * @return
     */
    boolean negative() default true;

    /**
     * 小数位
     * @return
     */
    int fraction() default 0;

    /**
     * 整数位
     * @return
     */
    int integer();

}
