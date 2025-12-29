package com.taoge.biz.es.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 场景标签
 */
public enum MerchantShopSceneTimeEnum {
    PARENTING("1", "亲子"),
    PARTY("2", "聚会"),
    DATE("3", "约会"),
    ;
    private final String value;
    private final String tagName;

    MerchantShopSceneTimeEnum(String value, String tagname) {
        this.value = value;
        this.tagName = tagname;
    }

    public static List<String> getTagValues() {
        List<String> tagValues = new ArrayList<>();
        for (MerchantShopSceneTimeEnum value : MerchantShopSceneTimeEnum.values()) {
            tagValues.add(value.getValue());
        }
        return tagValues;
    }

    public static List<String> getTagNames() {
        List<String> tagValues = new ArrayList<>();
        for (MerchantShopSceneTimeEnum value : MerchantShopSceneTimeEnum.values()) {
            tagValues.add(value.getTagName());
        }
        return tagValues;
    }

    public String getValue() {
        return value;
    }

    public String getTagName() {
        return tagName;
    }
}
