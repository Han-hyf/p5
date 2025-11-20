package com.taoge.biz.server.param.user;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class UserRegisterParam extends BaseParam {
    //    username    varchar(20)   not null comment '登录账号',
    //    password    varchar(32)   not null comment '登录密码',
    //    salt        char(4)       not null,
    //    mobile      varchar(20)   not null comment '手机号',
    @NotNull(name = "用户名")
    private String username;
    @NotNull(name = "密码")
    private String password;
    @NotNull(name = "手机号")
    private String mobile;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
