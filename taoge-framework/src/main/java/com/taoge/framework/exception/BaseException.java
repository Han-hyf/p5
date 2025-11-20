package com.taoge.framework.exception;

/**
 * 基础异常类
 * Created by xuejingtao
 */
public class BaseException extends RuntimeException {

    private int code;
    private Object data = null;

    public BaseException(int code) {
        super();
        this.code = code;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(int code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
