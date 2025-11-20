package com.taoge.api.user.vip.apply;

import com.taoge.biz.server.UserAccountServer;
import com.taoge.biz.server.param.vip.ApplyBuyVipParam;
import com.taoge.biz.server.vo.vip.ApplyBuyVipVO;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserVipApplyController extends BaseController<UserVipApplyParam> {

    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/apply")
    public ResponseData<?> execute(@RequestBody UserVipApplyParam param) {

        ApplyBuyVipParam applyBuyVipParam = param.convertTo(ApplyBuyVipParam.class);

        ResponseData<ApplyBuyVipVO> responseData = userAccountServer.applyBuyVip(applyBuyVipParam);
        return responseData;
    }
}
