package com.taoge.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.taoge.framework.annotation.JsonResultData;
import com.taoge.framework.common.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xuejingtao
 * 处理返回值，不返回null字段
 */
//@ControllerAdvice
public class ResponseBodyInterceptor implements ResponseBodyAdvice<ResponseData> {
    private static Logger logger = LoggerFactory.getLogger(ResponseBodyInterceptor.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return methodParameter.getParameterType() == ResponseData.class;
    }

    @Override
    public ResponseData beforeBodyWrite(ResponseData responseData, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        long endTime = System.currentTimeMillis();
        Object data = responseData.getData();
        convertNullData(data);
        logger.info("请求结束[URI:{}, 耗时:{}毫秒, result:{}]", request.getRequestURI(), endTime - Long.parseLong(request.getAttribute("interceptorStartTime").toString()), JSONObject.toJSONString(responseData));
        return responseData;
    }

    /**
     * 转化空值
     *
     * @param data
     */
    private void convertNullData(Object data) {
        if (null != data) {
            Method[] methods = data.getClass().getMethods();
            if (methods.length > 0) {
                for (Method getMethod : methods) {
                    try {
                        // 判断getXxx()方法，参数个数为0
                        if (getMethod.getName().startsWith("get") && getMethod.getParameterCount() == 0) {
                            Class returnType = getMethod.getReturnType();
                            Object resultData = getMethod.invoke(data);
                            if (returnType.isAnnotationPresent(JsonResultData.class)) {
                                if (resultData != null) {
                                    convertNullData(resultData);
                                }
                            }
                            if (resultData == null) {
                                // 获取setXxx() 方法
                                Method setMethod = data.getClass().getMethod(getMethod.getName().replaceFirst("g", "s"), returnType);
                                if (returnType == List.class) {
                                    // 集合类型，set一个新集合
                                    setMethod.invoke(data, new ArrayList<>());
                                }
                                if (returnType == String.class) {
                                    setMethod.invoke(data, "");
                                }
                                continue;
                            }
                            if (resultData instanceof Collection) {
                                calcCollection((Collection) resultData);
                            }
                            if (resultData instanceof PageInfo) {
                                calcPageInfo((PageInfo) resultData);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("convertNullData 异常, method:{} e:", getMethod.getName(), e);
                    }
                }
            }
        }
    }

    /**
     * 计算分页类型
     *
     * @param resultData
     */
    private void calcPageInfo(PageInfo resultData) {
        List list = resultData.getList();
        if (!CollectionUtils.isEmpty(list)) {
            calcCollection(list);
        }
    }

    /**
     * 计算集合类型
     *
     * @param resultData
     */
    private void calcCollection(Collection resultData) {
        if (!CollectionUtils.isEmpty(resultData)) {
            for (Object data : resultData) {
                convertNullData(data);
            }
        }
    }

}
