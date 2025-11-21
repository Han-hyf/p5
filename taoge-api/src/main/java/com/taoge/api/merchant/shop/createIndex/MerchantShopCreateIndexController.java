package com.taoge.api.merchant.shop.createIndex;

import com.taoge.biz.server.MerchantServer;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MerchantShopCreateIndexController extends BaseController<BaseParam> {
    @Resource
    MerchantServer merchantServer;

    @Guest
    @Override
    @PostMapping("/api/merchant/shop/createIndex")
    public ResponseData<?> execute(BaseParam param) {
        merchantServer.createIndex();
        return ResponseData.success();
    }
}
