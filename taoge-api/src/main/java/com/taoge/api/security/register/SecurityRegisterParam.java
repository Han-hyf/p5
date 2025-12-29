package com.taoge.api.security.register;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

/**
 * 用户注册参数
 */
@Data
public class SecurityRegisterParam extends BaseParam {
    @NotNull
    private String originMobile;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
}
