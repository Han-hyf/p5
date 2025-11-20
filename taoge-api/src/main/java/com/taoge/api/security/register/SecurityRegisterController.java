package com.taoge.api.security.register;

import com.taoge.api.common.redis.VerifyCodeRedisKey;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.errorCode.UserErrorCodeEnum;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.param.user.UserRegisterParam;
import com.taoge.biz.server.vo.user.UserRegisterVO;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.exception.BaseException;
import com.taoge.framework.util.UserContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SecurityRegisterController extends BaseController<SecurityRegisterParam> {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    UserServer userServer;
    @Resource
    VerifyCodeRedisKey verifyCodeRedisKey;

    @Guest
    @Override
    @PostMapping("/api/security/register")
    public ResponseData<?> execute(@RequestBody SecurityRegisterParam param) {
        //校验是否有标记
        UserInfo userInfo = UserContext.get();

        Long userId = userInfo.getUserId();
        String key = verifyCodeRedisKey.getValidateCodeMarkKey(userId,param.getMobile(), SmsActionType.REGISTER);
        String mark = stringRedisTemplate.opsForValue().get(key);
        if (null == mark){
            throw new BaseException(UserErrorCodeEnum.MARK_EXPIRE_ERROR.getCode(),UserErrorCodeEnum.MARK_EXPIRE_ERROR.getMsg());
        }
        //检查两次密码是否一致
        if (!param.getPassword().equals(param.getConfirmPassword())){
            throw new BaseException(UserErrorCodeEnum.PASSWORD_CONFIRM_ERROR.getCode(),UserErrorCodeEnum.PASSWORD_CONFIRM_ERROR.getMsg());

        }
        //调用注册接口
        UserRegisterParam userRegisterParam = param.convertTo(UserRegisterParam.class);
        UserRegisterVO userRegisterVO = userServer.register(userRegisterParam);
        return ResponseData.success();
    }
}
