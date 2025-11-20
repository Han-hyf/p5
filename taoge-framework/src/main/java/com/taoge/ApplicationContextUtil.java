package com.taoge;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存spring上下文工具
 */
@Configuration
public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    /**
     * bean缓存，省去每次重复查询上下文，不需要担心线程安全，每个class只会有一个对象
     */
    private static final ConcurrentHashMap<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextUtil.applicationContext == null) {
            ApplicationContextUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 按class获取bean
     *
     * @param clazz class
     * @param <T>   bean类型
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        Object t = beanMap.get(clazz);
        if (null == t) {
            t = applicationContext.getBean(clazz);
            // bean不该不存在，空指针属于代码问题
            beanMap.put(clazz, t);
        }
        return (T) t;
    }

    @Bean
    public ApplicationContextUtil getApplicationContextUtil() {
        return new ApplicationContextUtil();
    }
}
