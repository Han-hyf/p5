package com.taoge.api.security.login.username;

import com.taoge.framework.annotation.Mobile;
import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class SecurityLoginUsernameParam extends BaseParam {
    @NotNull(errorMsg = "请输入用户名")
    private String username;
    @NotNull(errorMsg = "请输入密码")
    private String password;

}
