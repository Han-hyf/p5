package com.taoge.biz.server.param.user;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class UserLoginByMobileParam extends BaseParam {

    @NotNull(name = "手机号")
    private String mobile;
}
