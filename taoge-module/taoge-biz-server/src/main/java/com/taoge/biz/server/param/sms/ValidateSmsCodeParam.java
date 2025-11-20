package com.taoge.biz.server.param.sms;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.framework.annotation.NotNull;
import lombok.Data;

@Data
public class ValidateSmsCodeParam {

    @NotNull(errorMsg = "请输入手机号")
    private String originMobile;
    @NotNull(errorMsg = "请选择业务类型")
    private SmsActionType actionType;
    @NotNull(errorMsg = "请输入验证码")
    private String code;

}
