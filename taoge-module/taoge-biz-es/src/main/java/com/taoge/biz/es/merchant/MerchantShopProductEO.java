package com.taoge.biz.es.merchant;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class MerchantShopProductEO extends BaseVO {
    @Field(type = FieldType.Keyword)
    private Long id;
    @Field(type = FieldType.Keyword)
    private String productName;
}
