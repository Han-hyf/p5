package com.taoge.api.user.vip.enable;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.UserAccountServer;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class userVipEnableController extends BaseController<IdParam> {
    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/enable")
    public ResponseData<?> execute(@RequestBody IdParam param) {
        userAccountServer.enableUserVipConfig(param);
        return ResponseData.success();
    }
}
