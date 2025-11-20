package com.taoge.api.user.vip.disable;

import com.taoge.biz.common.param.IdParam;
import com.taoge.biz.server.UserAccountServer;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class userVipDisableController extends BaseController<IdParam> {

    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/disable")
    public ResponseData<?> execute(@RequestBody IdParam param) {
        userAccountServer.disableUserVipConfig(param);
        return ResponseData.success();
    }
}
