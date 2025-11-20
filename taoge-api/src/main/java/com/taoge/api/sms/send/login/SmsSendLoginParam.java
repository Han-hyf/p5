package com.taoge.api.sms.send.login;

import com.taoge.framework.annotation.Mobile;
import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;


@Data
public class SmsSendLoginParam extends BaseParam {
    @NotNull(errorMsg = "请输入手机号")
    @Mobile
    private String originMobile;
    @NotNull(errorMsg = "请输入国家")
    private String mobilePrefix;

}
