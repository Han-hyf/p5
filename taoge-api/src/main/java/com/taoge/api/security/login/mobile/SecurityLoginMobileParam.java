package com.taoge.api.security.login.mobile;

import com.taoge.framework.annotation.Mobile;
import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class SecurityLoginMobileParam extends BaseParam {
    @NotNull(errorMsg = "请输入手机号")
    @Mobile
    private String originMobile;
    @NotNull(errorMsg = "请填写验证码")
    private String code;

}
