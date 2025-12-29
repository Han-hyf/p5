package com.taoge.framework.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.annotation.NotSign;
import com.taoge.framework.annotation.Param;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.constantsEnum.TokenTypeEnum;
import com.taoge.framework.exception.ParamException;
import com.taoge.framework.exception.TokenException;
import com.taoge.framework.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.net.SocketException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xuejingtao
 */
public abstract class BaseInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/static")) {
            return true;
        }
        if (uri.contains("/health")) {
            response.getWriter().write("ok");
            return false;
        }
        if (!uri.contains("/api")) {
            return true;
        }
        RequestId.createId();
        ThreadContext.put("req.id", UUIDUtil.randomUUID().toUpperCase());
        // 获得请求参数
        request.setAttribute("interceptorStartTime", System.currentTimeMillis());

        // 从request header中解析token信息，解析成UserInfo用户信息
        if (o instanceof HandlerMethod) {
            validateToken((HandlerMethod) o, request);
            validateParam((HandlerMethod) o, request);
        }
        return true;
    }

    private void validateToken(HandlerMethod handler, HttpServletRequest request) {
        NotSign notSign = handler.getMethodAnnotation(NotSign.class);
        if (null != notSign){
            return;
        }
        doValidateToken(handler, request);
    }

    protected abstract void doValidateToken(HandlerMethod handler, HttpServletRequest request);

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        RequestId.clear();
        doAfterCompletion();
    }

    protected abstract void doAfterCompletion();

    private void validateParam(HandlerMethod handlerMethod, HttpServletRequest request) throws Exception {// 获取参数
        Map<String, Object> paramMap = null;
        String method = request.getMethod();
        if (RequestMethod.POST.name().equals(method)) {
            ParamRequestWrapper paramRequestWrapper = new ParamRequestWrapper(request);
            String body = paramRequestWrapper.getBody();
            LinkedHashMap<String, Object> map = BeanMapUtil.toMap(JSON.parse(body));
            paramMap = (Map<String, Object>) map.get("innerMap");
        } else if (RequestMethod.GET.name().equals(method)) {
            paramMap = WebUtils.getParametersStartingWith(request, null);
        }
        logger.info("请求开始[URI:{}, Method:{}, Params:{}]", request.getRequestURI(), request.getMethod(), JSONObject.toJSON(paramMap));
        if (paramMap == null) {
            paramMap = new HashMap<>();
        }

        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        if (methodParameters.length > 0) {
            for (MethodParameter methodParameter : methodParameters) {
                Class<?> parameterType = methodParameter.getParameterType();
                if (parameterType.isAnnotationPresent(Param.class)) {
                    while (parameterType != null) {  // 遍历所有父类字节码对象
                        Field[] declaredFields = parameterType.getDeclaredFields();  // 获取字节码对象的属性对象数组
                        if (declaredFields.length > 0) {
                            for (Field field : declaredFields) {
                                ResponseData<?> responseData = ParamValidateUtil.validateParam(field, paramMap.get(field.getName()));
                                if (!responseData.isSuccess()) {
                                    throw new ParamException(responseData.getMsg());
                                }
                            }
                        }
                        parameterType = parameterType.getSuperclass();  // 获得父类的字节码对象
                    }
                }
            }
        }
    }
}
