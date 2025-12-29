package com.taoge.biz.common.errorCode;

/**
 * 订单错误码
 */
public enum OrderErrorCodeEnum {

    INIT_ORDER_EXISTS(40000, "您有待支付的订单"),
    BUSINESS_ORDER_NOT_EXISTS(40001, "业务订单不存在"),
    APPLY_ORDER_ERROR(40002, "创建订单失败，请重试"),
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
