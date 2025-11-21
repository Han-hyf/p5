package com.taoge.biz.server.param.merchant;

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

//    - 分类筛选
//- 人均价格筛选
//- 营业时间
//- 场景
//- 设施筛选
//- 关键字搜索
//  - 店铺名称
//  - 商品名称
//  - 排行榜标签
//  - 套餐包含的商品名称
//- 排序：价格、销量、好评
}
