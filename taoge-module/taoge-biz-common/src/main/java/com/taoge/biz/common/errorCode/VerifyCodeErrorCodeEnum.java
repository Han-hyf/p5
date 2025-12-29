package com.taoge.biz.common.errorCode;

/**
 * 验证码错误码
 */
public enum VerifyCodeErrorCodeEnum {

    SMS_CODE_SEND(10000, "您的短信验证码已发送"),
    SMS_CODE_WRONG(10001, "您的验证码错误"),
    SMS_CODE_NOT_EXISTS(10002, "短信验证码不存在，请先获取"),
    SMS_CODE_EXPIRE(10003, "短信验证码已过期，请重新获取"),
    SMS_CODE_FAIL(10003, "短信验证码校验失败，请重新获取"),
    ;


    private final int code;
    private final String msg;

    VerifyCodeErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
