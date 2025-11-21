package com.taoge.biz.es.enums;

public enum MerchantShopFacilitiesEnum {
    private_room("1"),// 包间
    baby_chair("2"),// 宝宝椅
    air_conditioning("3") // 空调
    ;
    private final String value;

    MerchantShopFacilitiesEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
