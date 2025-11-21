package com.taoge.biz.es.enums;

import java.util.ArrayList;
import java.util.List;

public enum MerchantShopTagOpenTimeEnum {
    open_time_0_5("1"),
    open_time_5_10("2"),
    open_time_10_14("3"),
    open_time_14_17("4"),
    open_time_17_21("5"),
    open_time_21_24("6"),
    open_time_all("7"),
    ;



    private final String value;

    MerchantShopTagOpenTimeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static List<String> getTagValues() {
        List<String> tagValues = new ArrayList<>();
        for (MerchantShopTagOpenTimeEnum value : MerchantShopTagOpenTimeEnum.values()) {
            tagValues.add(value.getValue());
        }
        return tagValues;
    }
}
