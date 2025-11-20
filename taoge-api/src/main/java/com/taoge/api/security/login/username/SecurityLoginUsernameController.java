package com.taoge.api.security.login.username;

import com.taoge.api.security.login.SecurityLoginVO;
import com.taoge.biz.server.UserServer;
import com.taoge.biz.server.param.user.UserLoginByMobileParam;
import com.taoge.biz.server.param.user.UserLoginByUsernameParam;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class SecurityLoginUsernameController extends BaseController<SecurityLoginUsernameParam> {

    @Resource
    UserServer userServer;


    @Guest
    @Override
    @PostMapping("/api/security/login/username")
    public ResponseData<?> execute(@RequestBody SecurityLoginUsernameParam param) {
        //TODO 安全校验,防止暴力撞库


        //登录
        UserLoginByUsernameParam userLoginByUsernameParam = new UserLoginByUsernameParam();
        userLoginByUsernameParam.setUsername(param.getUsername());
        userLoginByUsernameParam.setPassword(param.getPassword());

        UserLoginVO userLoginVO = userServer.loginByUsername(userLoginByUsernameParam);

        //不能直接返回userID,封装一个token返回
        UserInfo userInfo = TokenUtil.generateToken(userLoginVO.getUserId());
        SecurityLoginVO securityLoginVO = new SecurityLoginVO();
        securityLoginVO.setToken(userInfo.getToken());

        //如果是h5请求,可以直接setToken
        setToken(URLEncoder.encode(userInfo.getToken(), StandardCharsets.UTF_8));
        return ResponseData.success("",securityLoginVO);
    }
}
