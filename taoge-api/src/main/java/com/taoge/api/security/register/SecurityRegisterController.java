package com.taoge.api.security.register;

import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.common.errorCode.UserErrorCodeEnum;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.param.user.UserRegisterParam;
import com.taoge.biz.server.vo.user.UserRegisterVO;
import com.taoge.common.redis.VerifyCodeRedisKey;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.exception.BusinessException;
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

    @Guest
    @Override
    @PostMapping("/api/security/register")
    public ResponseData<?> execute(@RequestBody SecurityRegisterParam param) {
        Long userId = UserContext.get().getUserId();
        // 先查询校验通过标记
        String key = VerifyCodeRedisKey.getValidateCodeMarkKey(userId, param.getOriginMobile(), SmsActionType.REGISTER);
        String mark = stringRedisTemplate.opsForValue().get(key);
        if (null == mark) {
            throw new BusinessException(UserErrorCodeEnum.MARK_EXPIRE_ERROR.getCode(), UserErrorCodeEnum.MARK_EXPIRE_ERROR.getMsg());
        }

        if (!param.getPassword().equals(param.getConfirmPassword())) {
            throw new BusinessException(UserErrorCodeEnum.CONFIRM_PASSWORD_ERROR.getCode(), UserErrorCodeEnum.CONFIRM_PASSWORD_ERROR.getMsg());
        }

        UserRegisterParam userRegisterParam = param.convertTo(UserRegisterParam.class);
        userRegisterParam.setMobile(param.getOriginMobile());
        // 调用注册服务
        UserRegisterVO register = userServer.register(userRegisterParam);
        return ResponseData.success();
    }
}
