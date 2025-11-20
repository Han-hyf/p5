package com.taoge.framework.service;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xuejingtao
 * @since 2021/1/17 01:26
 **/
@Component
public class LoadMapperBeanPostProcess implements BeanPostProcessor, CommandLineRunner {

    private static final ConcurrentHashMap<Class<?>, Object> services = new ConcurrentHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof BaseService) {
            services.put(bean.getClass(), bean);
        }
        return bean;
    }

    @Override
    public void run(String... args) throws Exception {
        injectMapper();
    }

    private static void injectMapper() {
        services.forEach((key, value) -> {
            if(value instanceof BaseService) {
                ((BaseService<?, ?>) value).getMapper();
            }
        });
    }
}
