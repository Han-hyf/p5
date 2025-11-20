package com.taoge.biz.es.merchant;


import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Document(indexName = "merchant_shop")
public class MerchantShopEO {

    private Long id;
    private Long merchantId;
    private Long categoryId;
    private String shopName;
    private BigDecimal averageConsume;
    private Long saleVolume;
    private Double score;

}
