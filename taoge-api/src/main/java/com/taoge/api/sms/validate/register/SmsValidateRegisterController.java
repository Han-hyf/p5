package com.taoge.api.sms.validate.register;

import com.taoge.api.common.redis.VerifyCodeRedisKey;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.server.VerifyCodeServer;
import com.taoge.biz.server.param.sms.ValidateSmsCodeParam;
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
    @Resource
    VerifyCodeRedisKey verifyCodeRedisKey;


    @Guest
    @Override
    @PostMapping("/api/sms/validate/register")
    public ResponseData<?> execute(@RequestBody SmsValidateRegisterParam param) {
        //将前端传来的信息对象转换为server层所需要的对象，然后调用server层
        UserInfo userInfo = UserContext.get();
        Long userId = userInfo.getUserId();
        ValidateSmsCodeParam validateSmsCodeParam = param.convertTo(ValidateSmsCodeParam.class);
        validateSmsCodeParam.setActionType(SmsActionType.REGISTER);
        //validateSmsCodeParam.setUserId(userId);
        verifyCodeServer.validateSmsCode(validateSmsCodeParam);

        //验证成功后,添加一个成功的标记
        //key = 用户ID+手机号+业务类型
        String key = verifyCodeRedisKey.getValidateCodeMarkKey(userId,param.getOriginMobile(),SmsActionType.REGISTER);
        stringRedisTemplate.opsForValue().set(key,"1",10, TimeUnit.MINUTES);
        return ResponseData.success();
    }
}
