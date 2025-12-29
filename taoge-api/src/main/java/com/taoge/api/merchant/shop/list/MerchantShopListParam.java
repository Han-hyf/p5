package com.taoge.api.merchant.shop.list;

import com.taoge.biz.es.enums.MerchantShopSortTypeEnum;
import com.taoge.framework.annotation.Range;
import com.taoge.framework.controller.PageParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MerchantShopListParam extends PageParam {
    private Long categoryId;
    private BigDecimal averageConsumeStart;
    private BigDecimal averageConsumeEnd;
    private List<String> openTime;
    private List<String> scene;
    private List<String> facilities;
    private String searchKey;
    private String lat;
    private String lon;
    private double distance;
    private String shopName;
    private String productName;

    @Range(clazz = MerchantShopSortTypeEnum.class, key = "sort", errorMsg = "请选择排序")
    private int sortType;
}
