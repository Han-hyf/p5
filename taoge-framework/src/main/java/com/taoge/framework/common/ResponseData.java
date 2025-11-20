package com.taoge.framework.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by xuejingtao
 */
public class ResponseData<T> implements Serializable {

    private int code;
    private String msg;
    private T data;

    public ResponseData() {
    }

    public ResponseData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseData(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static ResponseData<?> success() {
        return new ResponseData<>(ErrorCode.SUCCESS_CODE, ErrorCode.SUCCESS_MSG);
    }

    public static ResponseData<?> success(String sucMsg) {
        return new ResponseData<>(ErrorCode.SUCCESS_CODE, sucMsg);
    }

    public static <T> ResponseData<T> success(String sucMsg, T data) {
        return new ResponseData<>(ErrorCode.SUCCESS_CODE, sucMsg, data);
    }

    public static ResponseData<?> error() {
        return new ResponseData<>(ErrorCode.ERROR_CODE, ErrorCode.ERROR_MSG);
    }

    public static <T> ResponseData<T> error(String errorMsg) {
        return new ResponseData<>(ErrorCode.ERROR_CODE, errorMsg);
    }

    public static <T> ResponseData<T> error(int code, String errorMsg) {
        return new ResponseData<>(code, errorMsg);
    }

    /**
     * 登录超时
     */
    public static ResponseData<?> loginTimeOut() {
        return new ResponseData<>(ErrorCode.LOGIN_TIME_OUT_CODE, ErrorCode.LOGIN_TIME_OUT_MSG);
    }

    /**
     * 未登录
     */
    public static ResponseData<?> notLogin() {
        return new ResponseData<>(ErrorCode.NOT_LOGIN_CODE, ErrorCode.NOT_LOGIN_MSG);
    }

    /**
     * 权限不足
     */
    public static ResponseData<?> roleError() {
        return new ResponseData<>(ErrorCode.ROLE_ERROR_CODE, ErrorCode.ROLE_ERROR_MSG);
    }

    /**
     * 参数验证失败
     */
    public static ResponseData<?> paramValidateFail(String message) {
        return new ResponseData<>(ErrorCode.PARAM_VALIDATE_FAIL, message);
    }

    /**
     * 参数验证失败
     *
     * @param message 错误消息
     * @param data    返回数据
     */
    public static ResponseData<?> paramValidateFail(String message, Object data) {
        return new ResponseData<>(ErrorCode.PARAM_VALIDATE_FAIL, message, data);
    }

    /**
     * 用户已失效
     */
    public static ResponseData<?> userInValidate() {
        return new ResponseData<>(ErrorCode.USER_INVALID_CODE, ErrorCode.USER_INVALID_MSG);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @JsonIgnore
    @JSONField(serialize = false)
    /**
     * 业务操作是否成功
     */
    public boolean isSuccess() {
        return code == ErrorCode.SUCCESS_CODE;
    }
}
