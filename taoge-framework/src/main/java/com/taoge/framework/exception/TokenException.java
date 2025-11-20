package com.taoge.framework.exception;

public class TokenException extends BaseException {
    public TokenException() {
        super(403, "身份异常");
    }

    public TokenException(String message) {
        super(403, message);
    }
}
