package com.taoge.biz.es.merchant;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(indexName = "merchant_shop")
public class MerchantShopEO {

    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    @Field(type = FieldType.Keyword)
    private Long merchantId;

    @Field(type = FieldType.Keyword)
    private Long categoryId;

    @Field(type = FieldType.Keyword,copyTo = "fullText")
    private String shopName;

    @Field(type = FieldType.Double)
    private BigDecimal averageConsume;

    @Field(type = FieldType.Long)
    private Long salesVolume;

    @Field(type = FieldType.Double)
    private Double score;

    @Field(type = FieldType.Keyword)
    private List<String> openTime;
    @Field(type = FieldType.Keyword)
    private List<String> scene;
    @Field(type = FieldType.Object)
    private List<MerchantShopTagEO> facilities;

    @Field(type = FieldType.Object)
    private List<MerchantShopProductEO> productList;

    @Field(type = FieldType.Keyword,copyTo = "fullText")
    private List<String> productNameList;

    @Field(type = FieldType.Text)
    private String fullText;

}
