package com.taoge.biz.server.param.user;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class UserLoginByUsernameParam extends BaseParam {
    @NotNull(name = "用户名")
    private String username;
    @NotNull(name = "密码")
    private String password;
}
