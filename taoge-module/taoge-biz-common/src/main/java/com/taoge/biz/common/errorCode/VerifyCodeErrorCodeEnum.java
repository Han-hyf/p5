package com.taoge.biz.common.errorCode;

/**
 * 验证码错误码
 */
public enum VerifyCodeErrorCodeEnum {

    SMS_CODE_SEND(1000,"你的短信验证码已发送"),
    SMS_CODE_WRONG(1001,"验证码错误"),
    SMS_CODE_NOT_EXISTS(1002,"您的验证码不存在"),
    SMS_CODE_EXPIRE(1003,"您的验证码已过期"),
    SMS_CODE_FAIL(1004,"错误次数过多，请重新发送短信验证码")
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
