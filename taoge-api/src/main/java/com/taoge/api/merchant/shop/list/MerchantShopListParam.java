package com.taoge.api.merchant.shop.list;

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
}
