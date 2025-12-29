package com.taoge.api.sms.send.register;

import com.taoge.framework.annotation.Mobile;
import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class SmsSendRegisterParam extends BaseParam {
    @NotNull(errorMsg = "请输入手机号")
    @Mobile
    private String originMobile;
    @NotNull(errorMsg = "请选择国家")
    private String mobilePrefix;
}
