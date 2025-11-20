package com.taoge.framework.exception;

import com.taoge.framework.common.ErrorCode;

/**
 * Created by xuejingtao
 */
public class BusinessException extends BaseException {

    public BusinessException(int code) {
        super(code);
    }

    public BusinessException(String message) {
        super(ErrorCode.BUSINESS_VALIDATE_FAIL, message);
    }

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(int code, String message, Object data) {
        super(code, message, data);
    }
}
