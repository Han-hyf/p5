package com.taoge.biz.es.merchant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.taoge.biz.es.enums.MerchantShopTagOpenTimeEnum;
import com.taoge.framework.controller.BaseVO;
import com.taoge.framework.es.common.GeoPointDeserializer;
import com.taoge.framework.es.common.GeoPointSerializer;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "merchant_shop")
public class MerchantShopEO extends BaseVO {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;
    @Field(type = FieldType.Keyword)
    private Long merchantId;
    @Field(type = FieldType.Keyword)
    private Long categoryId;
    @Field(type = FieldType.Keyword, copyTo = "fullText")
    private String shopName;
    @Field(type = FieldType.Double)
    private BigDecimal averageConsume;
    @Field(type = FieldType.Long)
    private Long salesVolume;
    @Field(type = FieldType.Double)
    private Double score;

    /**
     * 定位地址
     */
    @Field(type = FieldType.Keyword)
    private String address;
    private double lat;
    private double lon;

    /**
     * 营业时间
     * 方式1：存标签值，根据标签值搜索，返回结果再通过枚举转成显示的文案
     */
    @Field(type = FieldType.Keyword)
    private List<String> openTime;

    private List<String> openTimeStr;

    public List<String> getOpenTimeStr() {
        if (CollectionUtils.isEmpty(openTimeStr) && CollectionUtils.isNotEmpty(openTime)) {
            openTimeStr = new ArrayList<>();
            for (String s : openTime) {
                MerchantShopTagOpenTimeEnum e = MerchantShopTagOpenTimeEnum.getByValue(s);
                openTimeStr.add(e.getTagName());
            }
        }
        return openTimeStr;
    }

    /**
     * 场景
     * 方式2：直接存显示文案，根据文案标签搜索，直接返回结果
     */
    @Field(type = FieldType.Keyword)
    private List<String> scene;

    /**
     * 方式3：存标签对象，根据对象值搜索，返回标签对象，在前端显示标签对象名称
     */
    @Field(type = FieldType.Object)
    private List<MerchantShopTagEO> facilities;

    @Field(type = FieldType.Object)
    private List<MerchantShopProductEO> productList;

    @Field(type = FieldType.Keyword, copyTo = "fullText")
    private List<String> productNameList;

    @GeoPointField
    @JsonSerialize(using = GeoPointSerializer.class)
    @JsonDeserialize(using = GeoPointDeserializer.class)
    private GeoPoint location;

    @Field(type = FieldType.Text)
    private String fullText;
}
