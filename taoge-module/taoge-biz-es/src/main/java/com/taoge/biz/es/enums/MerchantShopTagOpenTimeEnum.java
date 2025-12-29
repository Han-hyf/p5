package com.taoge.biz.es.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 营业时间类型
 */
public enum MerchantShopTagOpenTimeEnum {
    open_time_0_5("1", "0-5时"),
    open_time_5_10("2", "5-10时"),
    open_time_10_14("3", "10-14时"),
    open_time_14_17("4", "14-17时"),
    open_time_17_21("5", "17-21时"),
    open_time_21_24("6", "21-24时"),
    open_time_all("7", "24小时营业"),
    ;
    private final String value;
    private final String tagName;

    MerchantShopTagOpenTimeEnum(String value, String tagName) {
        this.value = value;
        this.tagName = tagName;
    }

    public static List<String> getTagValues() {
        List<String> tagValues = new ArrayList<>();
        for (MerchantShopTagOpenTimeEnum value : MerchantShopTagOpenTimeEnum.values()) {
            tagValues.add(value.getValue());
        }
        return tagValues;
    }

    public static MerchantShopTagOpenTimeEnum getByValue(String value) {
        if (null == value) {
            return null;
        }
        for (MerchantShopTagOpenTimeEnum e : MerchantShopTagOpenTimeEnum.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getTagName() {
        return tagName;
    }
}
