package com.taoge.framework.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BeanMapUtil {

    /**
     * Converts a map to a JavaBean.
     * @param type type to convert
     * @param map map to convert
     * @return JavaBean converted
     */
    public static <T> T toBean(Class<T> type, Map<? extends Object, ? extends Object> map){
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            obj = type.newInstance();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = value;
                    descriptor.getWriteMethod().invoke(obj, args);
                }
            }
        } catch (InvocationTargetException | IntrospectionException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * Converts a JavaBean to a map.
     *
     * @param bean JavaBean to convert
     * @return map converted
     */
    public static LinkedHashMap<String, Object> toMap(Object bean) {
        if (null == bean) {
            return null;
        }
        LinkedHashMap<String, Object> returnMap = new LinkedHashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    returnMap.put(propertyName, result);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();    // failed to call setters
        } catch (IntrospectionException e) {
            e.printStackTrace();    // failed to get class fields
        } catch (IllegalAccessException e) {
            e.printStackTrace();    // failed to instant JavaBean
        }

        return returnMap;
    }

    /**
     * Converts a JavaBean to a map.
     *
     * @param bean JavaBean to convert
     * @return map converted
     */
    public static HashMap<String, Object> toHashMap(Object bean) {
        if (null == bean) {
            return null;
        }
        HashMap<String, Object> returnMap = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    returnMap.put(propertyName, result);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();    // failed to call setters
        } catch (IntrospectionException e) {
            e.printStackTrace();    // failed to get class fields
        } catch (IllegalAccessException e) {
            e.printStackTrace();    // failed to instant JavaBean
        }

        return returnMap;
    }

}
