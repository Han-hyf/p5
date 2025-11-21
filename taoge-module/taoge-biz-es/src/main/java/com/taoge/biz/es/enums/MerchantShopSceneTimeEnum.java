package com.taoge.biz.es.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景标签
 */
public enum MerchantShopSceneTimeEnum {
    PARENTING("1"),
    PARTY("2"),
    DATE("3"),
    ;
    private final String value;

    MerchantShopSceneTimeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getTagValues() {
        List<String> tagValues = new ArrayList<>();
        for (MerchantShopSceneTimeEnum value : MerchantShopSceneTimeEnum.values()) {
            tagValues.add(value.getValue());
        }
        return tagValues;
    }

}
