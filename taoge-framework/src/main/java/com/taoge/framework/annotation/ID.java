package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * @author xuejingtao
 * @since 2020/08/31 18:32
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface ID {

    String name() default "";

    String errorMsg() default "";

}
