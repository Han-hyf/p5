package com.taoge.biz.common.enums;

public enum BusinessTypeEnum {
    BUY_VIP,
    ;

    public static BusinessTypeEnum getByBusinessType(String businessType){
        if (null == businessType){
            return null;
        }
        for (BusinessTypeEnum e : BusinessTypeEnum.values()){
            if (e.name().equals(businessType)){
                return e;
            }
        }
        return null;
    }
}
