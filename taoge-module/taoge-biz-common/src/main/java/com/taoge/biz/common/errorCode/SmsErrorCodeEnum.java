package com.taoge.biz.common.errorCode;

/**
 * 验证码错误码
 */
public enum SmsErrorCodeEnum {


    SEND_SMS_MAX_COUNT(1002,"今日短信已达上限"),
    SEND_SMS_ISO_ERROR(1003,"暂时不支持你的国家"),
    BUSINESS_CONTROL(1004,"操作过快，请您稍后"),
    SEND_SMS_ERROR(1005,"短信验证码发送失败")
    ;


    private final int code;
    private final String msg;

    SmsErrorCodeEnum(int code, String msg) {
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
