package com.taoge.biz.server.param.user;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

/**
 * 用户注册参数
 */
@Data
public class UserRegisterParam extends BaseParam {
    @NotNull(name = "手机号")
    private String mobile;
    @NotNull(name = "用户名")
    private String username;
    @NotNull(name = "密码")
    private String password;
}
