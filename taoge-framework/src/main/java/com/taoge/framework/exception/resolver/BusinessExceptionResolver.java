package com.taoge.framework.exception.resolver;

import com.taoge.framework.common.ResponseData;
import com.taoge.framework.exception.BaseException;
import com.taoge.framework.exception.ParamException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by xuejingtao
 */
@ControllerAdvice
public class BusinessExceptionResolver {
    public final Logger logger = LogManager.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData<?> resolveException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.OK.value()); //设置状态码
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); //设置ContentType
        response.setCharacterEncoding("UTF-8"); //避免乱码
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        ResponseData<Object> responseData = ResponseData.error("系统异常,请重试");
        try {
            boolean isPrint = true;
            if (e instanceof BaseException) {
                BaseException be = (BaseException) e;
                responseData.setCode(be.getCode());
                responseData.setMsg(be.getMessage());
                responseData.setData(be.getData());
                if (e instanceof ParamException) {
                    isPrint = false;
                    logger.error("请求参数异常, e:{}", e.getMessage());
                }
            } else if (e instanceof InvocationTargetException) {
                if (((InvocationTargetException) e).getTargetException() instanceof BaseException) {
                    BaseException be = ((BaseException) ((InvocationTargetException) e).getTargetException());
                    responseData.setCode(be.getCode());
                    responseData.setMsg(be.getMessage());
                    responseData.setData(be.getData());
                }
            }
            if (isPrint) {
                logger.error("请求异常, e:", e);
            }
        } catch (Exception ex) {
            logger.error("ex:", ex);
        }
        return responseData;
    }
}
