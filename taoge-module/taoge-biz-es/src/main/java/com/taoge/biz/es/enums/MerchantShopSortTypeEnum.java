package com.taoge.biz.es.enums;

/**
 * 店铺搜索排序
 */
public enum MerchantShopSortTypeEnum {
    score_desc(3),
    sales_volume_desc(4),
    average_consume_asc(5),
    average_consume_desc(6),
    ;
    private final int sort;

    MerchantShopSortTypeEnum(int sort) {
        this.sort = sort;
    }

    public static MerchantShopSortTypeEnum getBySort(int sort) {
        if (sort == 0) {
            return null;
        }
        for (MerchantShopSortTypeEnum e : MerchantShopSortTypeEnum.values()) {
            if (e.getSort() == sort) {
                return e;
            }
        }
        return null;
    }

    public int getSort() {
        return sort;
    }
}
