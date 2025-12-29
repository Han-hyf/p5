package com.taoge.biz.server.param.sms;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

/**
 * 发送短信验证码参数
 */
@Data
public class SendSmsCodeParam extends BaseParam {
    @NotNull(errorMsg = "用户身份异常")
    private Long userId;
    @NotNull(errorMsg = "请输入手机号")
    private String originMobile;
    @NotNull(errorMsg = "请选择国家")
    private String mobilePrefix;
    @NotNull(errorMsg = "请选择国家")
    private String iso;
    @NotNull(errorMsg = "请选择业务类型")
    private SmsActionType actionType;
    @NotNull(errorMsg = "用户身份异常")
    private String ip;
}
