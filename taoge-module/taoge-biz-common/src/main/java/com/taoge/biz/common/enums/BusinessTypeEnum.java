package com.taoge.biz.common.enums;

/**
 * 订单业务类型
 */
public enum BusinessTypeEnum {
    BUY_VIP,
    ;

    public static BusinessTypeEnum getByBusinessType(String businessType) {
        if (null == businessType) {
            return null;
        }
        for (BusinessTypeEnum e : BusinessTypeEnum.values()) {
            if (e.name().equals(businessType)) {
                return e;
            }
        }
        return null;
    }

}
