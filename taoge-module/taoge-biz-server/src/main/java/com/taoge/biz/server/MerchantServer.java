package com.taoge.biz.server;

import com.github.pagehelper.PageInfo;
import com.taoge.biz.es.merchant.MerchantShopEO;
import com.taoge.biz.es.service.MerchantShopEsService;
import com.taoge.biz.server.param.merchant.SearchShopParam;
import com.taoge.framework.es.common.ElasticQueryEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class MerchantServer {
    @Resource
    MerchantShopEsService merchantShopEsService;

    public MerchantShopEO random() {
        return merchantShopEsService.random();
    }

    public void createIndex() {
        merchantShopEsService.createIndex();
    }

    public void deleteIndex() {
        merchantShopEsService.deleteIndex();
    }

    public GetIndexResponse getIndex() {
        return merchantShopEsService.getIndex();
    }

    public PageInfo<MerchantShopEO> searchShop(SearchShopParam param) {
        ElasticQueryEntity q = merchantShopEsService.buildQueryEntity(param.getPageNum(), param.getPageSize());
        // 分类筛选
        q.eq(null != param.getCategoryId() && param.getCategoryId() > 0, "categoryId", param.getCategoryId());
        // 价格区间
        q.gte(null != param.getAverageConsumeStart() && param.getAverageConsumeStart().compareTo(BigDecimal.ZERO) > 0, "averageConsume", param.getAverageConsumeStart());
        q.lte(null != param.getAverageConsumeEnd() && param.getAverageConsumeEnd().compareTo(BigDecimal.ZERO) > 0, "averageConsume", param.getAverageConsumeEnd());
        q.in(CollectionUtils.isNotEmpty(param.getOpenTime()), "openTime", param.getOpenTime());
        q.in(CollectionUtils.isNotEmpty(param.getScene()), "scene", param.getScene());
        q.in(CollectionUtils.isNotEmpty(param.getFacilities()), "facilities.tabValue", param.getFacilities());
        q.analysis(StringUtils.isNotBlank(param.getSearchKey()), "fullText", param.getSearchKey());
//        private String searchKey;
        PageInfo<MerchantShopEO> pageInfo = merchantShopEsService.pageList(q);
        return pageInfo;
    }
}
