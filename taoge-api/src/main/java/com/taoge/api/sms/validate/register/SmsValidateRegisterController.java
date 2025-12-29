package com.taoge.api.sms.validate.register;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.server.VerifyCodeServer;
import com.taoge.biz.server.param.sms.ValidateSmsCodeParam;
import com.taoge.common.redis.VerifyCodeRedisKey;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.util.UserContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
public class SmsValidateRegisterController extends BaseController<SmsValidateRegisterParam> {
    @Resource
    VerifyCodeServer verifyCodeServer;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Guest
    @Override
    @PostMapping("/api/sms/validate/register")
    public ResponseData<?> execute(@RequestBody SmsValidateRegisterParam param) {
        UserInfo userInfo = UserContext.get();

        ValidateSmsCodeParam validateSmsCodeParam = param.convertTo(ValidateSmsCodeParam.class);
        validateSmsCodeParam.setActionType(SmsActionType.REGISTER);
        verifyCodeServer.validateSmsCode(validateSmsCodeParam);

        // 增加短信验证码校验通过标记
        // 用户Id+手机号+业务类型
        String key = VerifyCodeRedisKey.getValidateCodeMarkKey(userInfo.getUserId(), param.getOriginMobile(), SmsActionType.REGISTER);
        stringRedisTemplate.opsForValue().set(key, "1", 10, TimeUnit.MINUTES);
        return ResponseData.success();
    }
}
