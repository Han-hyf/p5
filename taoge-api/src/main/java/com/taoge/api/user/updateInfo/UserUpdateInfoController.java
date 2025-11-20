package com.taoge.api.user.updateInfo;

import com.taoge.biz.common.enums.GenderEnum;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.param.user.UpdateUserInfoParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserUpdateInfoController extends BaseController<UserUpdateInfoParam> {

    @Resource
    UserServer userServer;

    @Override
    @PostMapping("/api/user/updateInfo")
    public ResponseData<?> execute(@RequestBody UserUpdateInfoParam param) {

        UpdateUserInfoParam updateUserInfoParam = param.convertTo(UpdateUserInfoParam.class);
        //将Integer类型的性别改为枚举类型的
        GenderEnum genderEnum = GenderEnum.getByGender(param.getGender());
        updateUserInfoParam.setGender(genderEnum);

        userServer.updateUserInfo(updateUserInfoParam);

        return ResponseData.success();
    }
}
