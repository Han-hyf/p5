package com.taoge.api.security.getToken;

import com.taoge.framework.annotation.NotSign;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.common.UserInfo;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import com.taoge.framework.util.TokenUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SecurityGetTokenController extends BaseController {


    @Override
    @NotSign
    @PostMapping("api/security/getToken")
    public ResponseData<?> execute(BaseParam param) {
        UserInfo userInfo = TokenUtil.generateToken(null);
        return ResponseData.success("",userInfo.getToken());
    }
}
