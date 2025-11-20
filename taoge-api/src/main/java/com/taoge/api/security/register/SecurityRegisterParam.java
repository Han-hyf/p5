package com.taoge.api.security.register;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;


/**
 * 用户注册参数
 *
 */
@Data
public class SecurityRegisterParam extends BaseParam {
    @NotNull(name = "用户名")
    private String username;

    @NotNull(name = "密码")
    private String password;

    @NotNull(name = "密码")
    private String confirmPassword;

    @NotNull(name = "手机号")
    private String mobile;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
