package com.taoge.biz.common.errorCode;

/**
 * 验证码错误码
 */
public enum SmsErrorCodeEnum {

    SEND_SMS_DAY_MAX_COUNT_ERROR(20000, "当日发送短信已超上限，请明日重试"),
    SEND_SMS_ISO_ERROR(20001, "暂时不支持您的国家"),
    BUSINESS_CONTROL(20002, "操作过快，请稍后"),
    SEND_MSG_ERROR(20003, "短信验证码发送失败"),
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
