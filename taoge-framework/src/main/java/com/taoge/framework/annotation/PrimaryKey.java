package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by xuejingtao
 * entity类，主键注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface PrimaryKey {
    boolean autoIncrement() default false;
}
