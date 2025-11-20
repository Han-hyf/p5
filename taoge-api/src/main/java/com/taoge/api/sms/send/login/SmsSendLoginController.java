package com.taoge.api.sms.send.login;

import com.taoge.api.sms.send.register.SmsSendRegisterParam;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.errorCode.UserErrorCodeEnum;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.VerifyCodeServer;
import com.taoge.biz.server.param.sms.SendSmsCodeParam;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.annotation.NotNull;
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
public class SmsSendLoginController extends BaseController<SmsSendLoginParam>{

    @Resource
    VerifyCodeServer verifyCodeServer;
    @Resource
    UserServer userServer;


    @Guest
    @PostMapping("/api/sms/send/login")
    public ResponseData<?> execute(@RequestBody SmsSendLoginParam param) {
        UserInfo userInfo = UserContext.get();

        //校验是否已经被注册,如没有被注册则返回提示
        boolean isRegister = userServer.validateMobileIsRegister(param.getOriginMobile());
        if (!isRegister){
            throw new BusinessException(UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getCode(),UserErrorCodeEnum.MOBILE_NOT_EXISTS_ERROR.getMsg());
        }

        SendSmsCodeParam sendSmsCodeParam = param.convertTo(SendSmsCodeParam.class);
        sendSmsCodeParam.setActionType(SmsActionType.LOGIN);
        sendSmsCodeParam.setUserId(userInfo.getUserId());
        sendSmsCodeParam.setIp("127.0.0.1");
        sendSmsCodeParam.setIso("+86");
        verifyCodeServer.sendSmsCode(sendSmsCodeParam,"param");
        return ResponseData.success();
    }


}
