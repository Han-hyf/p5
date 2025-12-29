package com.taoge.framework.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by xuejingtao
 */
@Configuration
public class BusinessWebConfig implements WebMvcConfigurer {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public ApiInterceptor getApiInterceptor() {
        return new ApiInterceptor();
    }

    @Bean
    public CmsInterceptor getCmsInterceptor() {
        return new CmsInterceptor();
    }

    @Bean
    public SysInterceptor getSysInterceptor() {
        return new SysInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 判断是cms项目，走cms的拦截器
        if ("taoge-cms".equals(applicationName)) {
            registry.addInterceptor(new CmsInterceptor()).addPathPatterns("/**");
        } else if ("taoge-api".equals(applicationName)) {
            registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/**");
        } else if ("taoge-sys".equals(applicationName)) {
            registry.addInterceptor(new SysInterceptor()).addPathPatterns("/**");
        }
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
