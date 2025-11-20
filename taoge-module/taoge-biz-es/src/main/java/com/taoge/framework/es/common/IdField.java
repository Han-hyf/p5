package com.taoge.framework.es.common;

import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * id主键字段
 */
@Data
public class IdField {
    /**
     * 字段
     */
    private Field field;
    /**
     * get方法
     */
    private Method getMethod;
    /**
     * 类
     */
    private Class<?> clazz;
}
