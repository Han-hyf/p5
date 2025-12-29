package com.taoge.api.security.login.username;

import com.taoge.api.security.login.SecurityLoginVO;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.param.user.UserLoginByUsernameParam;
import com.taoge.biz.server.vo.user.UserLoginVO;
import com.taoge.framework.annotation.Guest;
import com.taoge.framework.annotation.NotSign;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.util.TokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@RestController
public class SecurityLoginUsernameController extends BaseController<SecurityLoginUsernameParam> {
    @Resource
    UserServer userServer;

    @NotSign
    @Override
    @PostMapping("/api/security/login/username")
    public ResponseData<?> execute(@RequestBody SecurityLoginUsernameParam param) {
        // TODO 安全校验拦截，防止暴力撞库

        // 调用登录服务
        UserLoginByUsernameParam userLoginByUsernameParam = param.convertTo(UserLoginByUsernameParam.class);
        UserLoginVO userLoginVO = userServer.loginByUsername(userLoginByUsernameParam);

        // 生成token
        UserInfo userInfo = TokenUtil.generateUserToken(userLoginVO.getUserId());
        SecurityLoginVO securityLoginVO = new SecurityLoginVO();
        securityLoginVO.setToken(userInfo.getToken());

        // 如果是h5请求，可以直接setToken
        // encode
        setUserToken(URLEncoder.encode(userInfo.getToken(), StandardCharsets.UTF_8));

        return ResponseData.success("", securityLoginVO);
    }
}
