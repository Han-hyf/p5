package com.taoge.biz.common.errorCode;

/**
 * 验证码错误码
 */
public enum UserErrorCodeEnum {

    MOBILE_EXISTS_ERROR(30000, "手机号已注册"),
    MARK_EXPIRE_ERROR(30001, "您的短信验证信息已过期，请重新获取"),
    CONFIRM_PASSWORD_ERROR(30002, "两次密码输入不一致"),
    USERNAME_EXISTS_ERROR(30003, "用户名已注册"),
    MOBILE_NOT_EXISTS_ERROR(30004, "手机号未注册"),
    USERNAME_LOGIN_ERROR(30005, "用户名或密码错误"),

    VIP_CONFIG_NOT_EXISTS(30006, "您选择的vip已下架，请重新选择"),
    ;

    private final int code;
    private final String msg;

    UserErrorCodeEnum(int code, String msg) {
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
