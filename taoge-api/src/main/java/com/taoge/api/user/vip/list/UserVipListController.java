package com.taoge.api.user.vip.list;

import com.taoge.biz.server.UserAccountServer;
import com.taoge.biz.server.param.vip.AddUserVipConfigParam;
import com.taoge.biz.server.param.vip.UpdateUserVipConfigParam;
import com.taoge.biz.server.param.vip.UserVipConfigListParam;
import com.taoge.biz.server.vo.vip.UserVipConfigVO;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserVipListController extends BaseController<BaseParam> {

    @Resource
    UserAccountServer userAccountServer;

    @Override
    @PostMapping("/api/user/vip/list")
    public ResponseData<?> execute(@RequestBody BaseParam param) {
        UserVipConfigListParam userVipConfigListParam = new UserVipConfigListParam();
        List<UserVipConfigVO> userVipConfigVOS = userAccountServer.userVipConfigList(userVipConfigListParam);
        return ResponseData.success("",userVipConfigVOS);
    }
}
