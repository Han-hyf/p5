package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * 校验银行卡号
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface BankCode {
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
