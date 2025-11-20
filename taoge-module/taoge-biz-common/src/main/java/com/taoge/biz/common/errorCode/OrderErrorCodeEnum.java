package com.taoge.biz.common.errorCode;

/**
 * 订单错误码
 */
public enum OrderErrorCodeEnum {


    INIT_EXIST_ERROR(40001,"您有未支付的订单"),
    BUSINESS_ORDER_NOT_EXISTS(40002,"业务订单不存在"),
    APPLY_ORDER_ERROR(40003,"订单创建失败")

    ;


    private final int code;
    private final String msg;

    OrderErrorCodeEnum(int code, String msg) {
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
