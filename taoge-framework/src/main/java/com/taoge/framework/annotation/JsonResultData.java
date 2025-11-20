package com.taoge.framework.annotation;

import java.lang.annotation.*;

/**
 * 数据对象，统一格式处理：字符串为null，转为""
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface JsonResultData {
}
