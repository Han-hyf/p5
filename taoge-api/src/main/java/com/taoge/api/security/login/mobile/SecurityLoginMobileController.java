package com.taoge.api.security.login.mobile;

import com.taoge.api.security.login.SecurityLoginVO;
import com.taoge.biz.common.enums.SmsActionType;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.VerifyCodeServer;
import com.taoge.biz.server.param.sms.ValidateSmsCodeParam;
import com.taoge.biz.server.param.user.UserLoginByMobileParam;
import com.taoge.biz.server.vo.user.UserLoginVO;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.util.TokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SecurityLoginMobileController extends BaseController<SecurityLoginMobileParam> {
    @Resource
    VerifyCodeServer verifyCodeServer;
    @Resource
    UserServer userServer;

    @Guest
    @Override
    @PostMapping("/api/security/login/mobile")
    public ResponseData<?> execute(@RequestBody SecurityLoginMobileParam param) {
        // 校验短信验证码
        ValidateSmsCodeParam validateSmsCodeParam = param.convertTo(ValidateSmsCodeParam.class);
        validateSmsCodeParam.setActionType(SmsActionType.LOGIN);
        verifyCodeServer.validateSmsCode(validateSmsCodeParam);

        // 登录
        UserLoginByMobileParam userLoginByMobileParam = new UserLoginByMobileParam();
        userLoginByMobileParam.setMobile(param.getOriginMobile());
        UserLoginVO userLoginVO = userServer.loginByMobile(userLoginByMobileParam);

        // 生成token
        UserInfo userInfo = TokenUtil.generateUserToken(userLoginVO.getUserId());
        SecurityLoginVO securityLoginVO = new SecurityLoginVO();
        securityLoginVO.setToken(userInfo.getToken());

        // 如果是h5请求，可以直接setToken
        setUserToken(userInfo.getToken());

        return ResponseData.success("", securityLoginVO);
    }
}
