package com.taoge.api.security.login.username;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class SecurityLoginUsernameParam extends BaseParam {
    @NotNull(name = "用户名")
    private String username;
    @NotNull(name = "密码")
    private String password;
}
