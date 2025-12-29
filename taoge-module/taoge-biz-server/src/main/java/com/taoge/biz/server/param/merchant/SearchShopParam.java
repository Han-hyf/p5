package com.taoge.biz.server.param.merchant;

import com.taoge.biz.es.enums.MerchantShopSortTypeEnum;
import com.taoge.framework.controller.PageParam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SearchShopParam extends PageParam {
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

    private MerchantShopSortTypeEnum sortType;

}
