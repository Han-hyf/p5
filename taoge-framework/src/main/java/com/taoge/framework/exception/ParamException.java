package com.taoge.framework.exception;

import com.taoge.framework.common.ErrorCode;

/**
 * 参数异常
 * Created by xuejingtao
 */
public class ParamException extends BaseException {

    public ParamException(int code) {
        super(code);
    }

    public ParamException(String message) {
        super(ErrorCode.PARAM_VALIDATE_FAIL, message);
    }

    public ParamException(int code, String message) {
        super(code, message);
    }
}
