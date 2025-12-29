package com.taoge.api.user.vip.sort;

import com.taoge.biz.server.UserAccountServer;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserVipSortController extends BaseController<UserVipSortParam> {
    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/sort")
    public ResponseData<?> execute(@RequestBody UserVipSortParam param) {
        userAccountServer.sortVipConfig(param.getIds());
        return ResponseData.success();
    }
}
