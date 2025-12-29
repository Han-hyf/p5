package com.taoge.biz.es.enums;

public enum MerchantShopFacilitiesEnum {
    private_room("1", "包间"),// 包间
    baby_chair("2", "宝宝椅"),// 宝宝椅
    air_conditioning("3", "空调") // 空调
    ;
    private final String value;
    private final String tagName;

    MerchantShopFacilitiesEnum(String value, String tagName) {
        this.value = value;
        this.tagName = tagName;
    }

    public String getValue() {
        return value;
    }

    public String getTagName() {
        return tagName;
    }
}
