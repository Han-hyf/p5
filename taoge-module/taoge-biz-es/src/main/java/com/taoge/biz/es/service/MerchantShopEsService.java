package com.taoge.biz.es.service;

import com.taoge.biz.es.merchant.MerchantShopEO;
import com.taoge.biz.es.util.GenerateRandomDataUtil;
import com.taoge.framework.es.service.ElasticService;
import org.springframework.stereotype.Service;

@Service
public class MerchantShopEsService extends ElasticService<MerchantShopEO> {

    public MerchantShopEO random() {
        MerchantShopEO merchantShopEO = GenerateRandomDataUtil.generatorMerchantShopEO();
        save(merchantShopEO);
        return merchantShopEO;
    }
}
