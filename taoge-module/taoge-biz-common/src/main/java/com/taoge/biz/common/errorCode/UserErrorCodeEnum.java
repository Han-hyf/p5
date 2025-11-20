package com.taoge.biz.common.errorCode;

/**
 * 用户错误码
 */
public enum UserErrorCodeEnum {


    MOBILE_EXISTS_ERROR(30000,"该手机号已注册"),
    MARK_EXPIRE_ERROR(30001,"您的短信验证信息已过期,请重新获取"),
    PASSWORD_CONFIRM_ERROR(3002,"两次密码不一致,请重新输入"),
    USERNAME_EXISTS_ERROR(3003,"用户名已存在,请换一个"),
    MOBILE_NOT_EXISTS_ERROR(3004,"改手机号还没注册"),
    PASSWORD_WRONG_ERROR(3005,"用户名或密码错误"),
    VIP_CONFIG_NOT_EXIST(3006,"您选择的VIP已下架"),
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
