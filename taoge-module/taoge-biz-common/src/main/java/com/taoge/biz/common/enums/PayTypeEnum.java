package com.taoge.biz.common.enums;

/**
 * 支付方式
 */
public enum PayTypeEnum {
    WX_PAY,
    ALI_PAY
    ;

    /**
     * 根据支付类型查询
     */
    public static PayTypeEnum getByPayType(String payType) {
        if (null == payType) {
            return null;
        }
        for (PayTypeEnum e : PayTypeEnum.values()) {
            if (e.name().equals(payType)) {
                return e;
            }
        }
        return null;
    }
}
