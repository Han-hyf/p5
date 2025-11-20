package com.taoge.framework.controller;

import org.springframework.beans.BeanUtils;

public class BasePO {

    /**
     * 转成其他类型
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T convertTo(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(this, t);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 将对象转成自己
     *
     * @param o
     */
    public void convertFrom(Object o) {
        if (null != o) {
            try {
                BeanUtils.copyProperties(o, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将对象转成指定类型
     *
     * @param o
     * @param clazz
     * @param <T>
     */
    public static <T> T convertFrom(Object o, Class<T> clazz) {
        T t = null;
        if (null != o) {
            try {
                t = clazz.newInstance();
                BeanUtils.copyProperties(o, t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return t;
    }
}
