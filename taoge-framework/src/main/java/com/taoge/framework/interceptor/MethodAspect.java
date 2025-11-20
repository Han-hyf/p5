package com.taoge.framework.interceptor;

import com.taoge.framework.annotation.Param;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.exception.ParamException;
import com.taoge.framework.util.ParamValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
@Slf4j
public class MethodAspect {
    @Pointcut("execution(* com.taoge.biz.**.server.*.*(..)) || execution(* com.taoge.biz.**.service.**.*.*(..))")
    public void Pointcut() {
    }

    @Around("Pointcut()")
    public Object Around(ProceedingJoinPoint pjp) throws Throwable {
        //获取连接点参数
        Object[] args = pjp.getArgs();

        for (Object methodParameter : args) {
            if (null == methodParameter) {
                continue;
            }
            Class<?> parameterType = methodParameter.getClass();
            if (parameterType.isAnnotationPresent(Param.class)) {
                while (parameterType != null) {  // 遍历所有父类字节码对象
                    Field[] declaredFields = parameterType.getDeclaredFields();  // 获取字节码对象的属性对象数组
                    if (declaredFields.length > 0) {
                        for (Field field : declaredFields) {
                            field.setAccessible(true);
                            ResponseData<?> responseData = ParamValidateUtil.validateParam(field, field.get(methodParameter));
                            if (!responseData.isSuccess()) {
                                throw new ParamException(responseData.getMsg());
                            }
                        }
                    }
                    parameterType = parameterType.getSuperclass();  // 获得父类的字节码对象
                }
            }
        }
        return pjp.proceed();
    }

}
