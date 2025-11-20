package com.taoge.biz.common.enums;

public enum PayTypeEnum {
    WX_PAY,
    ALI_PAY
    ;

    public static PayTypeEnum getByPayType(String payType){
        if (null == payType){
            return null;
        }
        for (PayTypeEnum e : PayTypeEnum.values()){
            if (e.name().equals(payType)){
                return e;
            }
        }
        return null;
    }

}
