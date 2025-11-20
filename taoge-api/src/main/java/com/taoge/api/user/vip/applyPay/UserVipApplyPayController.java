package com.taoge.api.user.vip.applyPay;

import com.taoge.biz.common.enums.PayTypeEnum;
import com.taoge.biz.server.UserAccountServer;
import com.taoge.biz.server.param.vip.ApplyPayBuyVipParam;
import com.taoge.biz.server.vo.vip.ApplyPayBuyVipVO;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserVipApplyPayController extends BaseController <UserVipApplyPayParam>{

    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/applyPay")
    public ResponseData<?> execute(@RequestBody UserVipApplyPayParam param) {

        ApplyPayBuyVipParam applyPayBuyVipParam = new ApplyPayBuyVipParam();
        applyPayBuyVipParam.setBusinessOrderSn(param.getBusinessOrderSn());
        applyPayBuyVipParam.setPayType(PayTypeEnum.getByPayType(param.getPayType()));
        ApplyPayBuyVipVO vo = userAccountServer.applyPayBuyVip(applyPayBuyVipParam);
        return ResponseData.success("",vo);

    }
}
