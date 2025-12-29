package com.taoge.api.sms.send.register;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.errorCode.UserErrorCodeEnum;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.VerifyCodeServer;
import com.taoge.biz.server.param.sms.SendSmsCodeParam;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.exception.BusinessException;
import com.taoge.framework.util.UserContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SmsSendRegisterController extends BaseController<SmsSendRegisterParam> {
    @Resource
    VerifyCodeServer verifyCodeServer;
    @Resource
    UserServer userServer;

    @Guest
    @Override
    @PostMapping("/api/sms/send/register")
    public ResponseData<?> execute(@RequestBody SmsSendRegisterParam param) {
        UserInfo userInfo = UserContext.get();
        // 校验手机号是否已经注册，已注册手机号返回提示
        boolean isRegister = userServer.validateMobileIsRegister(param.getOriginMobile());
        if (isRegister) {
            throw new BusinessException(UserErrorCodeEnum.MOBILE_EXISTS_ERROR.getCode(), UserErrorCodeEnum.MOBILE_EXISTS_ERROR.getMsg());
        }

        SendSmsCodeParam sendSmsCodeParam = param.convertTo(SendSmsCodeParam.class);
        sendSmsCodeParam.setUserId(userInfo.getUserId());
        sendSmsCodeParam.setIso("CN");
        sendSmsCodeParam.setActionType(SmsActionType.REGISTER);
        sendSmsCodeParam.setIp("127.0.0.1");
        verifyCodeServer.sendSmsCode(sendSmsCodeParam);
        return ResponseData.success();
    }
}
