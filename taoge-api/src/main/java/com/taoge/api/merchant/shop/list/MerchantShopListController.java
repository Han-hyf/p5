package com.taoge.api.merchant.shop.list;

import com.taoge.biz.es.enums.MerchantShopSortTypeEnum;
import com.taoge.biz.server.MerchantServer;
import com.taoge.biz.server.param.merchant.SearchShopParam;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MerchantShopListController extends BaseController<MerchantShopListParam> {
    @Resource
    MerchantServer merchantServer;

    @Guest
    @Override
    @PostMapping("/api/merchant/shop/list")
    public ResponseData<?> execute(@RequestBody MerchantShopListParam param) {
        SearchShopParam searchShopParam = param.convertTo(SearchShopParam.class);
        searchShopParam.setSortType(MerchantShopSortTypeEnum.getBySort(param.getSortType()));
        return ResponseData.success("", merchantServer.searchShop(searchShopParam));
    }
}
