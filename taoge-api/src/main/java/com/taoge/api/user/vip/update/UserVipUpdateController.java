package com.taoge.api.user.vip.update;

import com.taoge.biz.server.UserAccountServer;
import com.taoge.biz.server.param.vip.UpdateUserVipConfigParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserVipUpdateController extends BaseController<UserVipUpdateParam> {
    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/update")
    public ResponseData<?> execute(@RequestBody UserVipUpdateParam param) {
        UpdateUserVipConfigParam updateUserVipConfigParam = param.convertTo(UpdateUserVipConfigParam.class);
        userAccountServer.updateUserVipConfig(updateUserVipConfigParam);
        return ResponseData.success();
    }
}
