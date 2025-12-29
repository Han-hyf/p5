package com.taoge.api.user.vip.list;

import com.taoge.biz.server.UserAccountServer;
import com.taoge.biz.server.param.vip.AddUserVipConfigParam;
import com.taoge.biz.server.param.vip.UserVipConfigListParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserVipListController extends BaseController<BaseParam> {
    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/list")
    public ResponseData<?> execute(BaseParam param) {
        UserVipConfigListParam userVipConfigListParam = new UserVipConfigListParam();
        userVipConfigListParam.setStatus(true);
        return ResponseData.success("", userAccountServer.userVipConfigList(userVipConfigListParam));
    }
}
