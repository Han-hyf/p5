package com.taoge.api.user.vip.add;

import com.taoge.biz.server.UserAccountServer;
import com.taoge.biz.server.param.vip.AddUserVipConfigParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserVipAddController extends BaseController<UserVipAddParam> {
    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/add")
    public ResponseData<?> execute(@RequestBody UserVipAddParam param) {
        AddUserVipConfigParam addUserVipConfigParam = param.convertTo(AddUserVipConfigParam.class);
        userAccountServer.addUserVipConfig(addUserVipConfigParam);
        return ResponseData.success();
    }
}
