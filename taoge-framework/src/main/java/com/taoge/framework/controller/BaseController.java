package com.taoge.framework.controller;

import com.taoge.framework.common.ResponseData;
import com.taoge.framework.constantsEnum.TokenTypeEnum;
import com.taoge.framework.util.ControllerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class BaseController<RequestParam extends BaseParam> {

    private static final String domain = "www.taoge.cn";

    private final InheritableThreadLocal<BaseRequest> inheritableThreadLocal = new InheritableThreadLocal<BaseRequest>() {
        protected BaseRequest childValue(BaseRequest baseRequest) {
            return baseRequest == null ? null : BaseRequest.builder().build();
        }
    };
    protected final Logger logger = LogManager.getLogger(getClass());

    @ModelAttribute // 会在Controller方法被调用前执行
    public void setReqAndRes(HttpServletRequest req, HttpServletResponse res) {
        BaseRequest baseRequest = BaseRequest.builder()
                .request(req)
                .response(res)
                .session(req.getSession())
                .build();
        inheritableThreadLocal.set(baseRequest);
    }

    protected HttpServletRequest getRequest() {
        return inheritableThreadLocal.get().getRequest();
    }

    protected HttpServletResponse getResponse() {
        return inheritableThreadLocal.get().getResponse();
    }

    protected HttpSession getSession() {
        return inheritableThreadLocal.get().getSession();
    }

    @ResponseBody
    public abstract ResponseData<?> execute(RequestParam param);

    protected void setUserToken(String token) {
        ControllerUtil.setToken(token, TokenTypeEnum.TOKEN, getResponse());
    }

    protected void cleanUserToken() {
        ControllerUtil.cleanToken(TokenTypeEnum.TOKEN, getResponse());
    }

    protected void setMerchantToken(String token) {
        ControllerUtil.setToken(token, TokenTypeEnum.MERCHANT_TOKEN, getResponse());
    }

    protected void cleanMerchantToken() {
        ControllerUtil.cleanToken(TokenTypeEnum.MERCHANT_TOKEN, getResponse());
    }

    protected void setSysUserToken(String token) {
        ControllerUtil.setToken(token, TokenTypeEnum.SYS_TOKEN, getResponse());
    }

    protected void cleanSysUserToken() {
        ControllerUtil.cleanToken(TokenTypeEnum.SYS_TOKEN, getResponse());
    }

}
