package com.taoge.api.merchant.shop.deleteIndex;

import com.taoge.biz.server.MerchantServer;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MerchantShopDeleteIndexController extends BaseController<BaseParam> {
    @Resource
    MerchantServer merchantServer;

    @Guest
    @Override
    @PostMapping("/api/merchant/shop/deleteIndex")
    public ResponseData<?> execute(BaseParam param) {
        merchantServer.deleteIndex();
        return ResponseData.success();
    }
}
