package com.taoge.api.notify;

import com.taoge.biz.server.NotifyServer;
import com.taoge.biz.server.param.notify.WxPayNotifyParam;
import com.taoge.framework.common.ResponseData;
import com.taoge.framework.controller.BaseController;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class wxPayCallBackController extends BaseController<wxPayCallBackParam> {

    @Resource
    NotifyServer notifyServer;

    @Override
    @PostMapping("/api/notify/wxPayCallback")
    public ResponseData<?> execute(@RequestBody wxPayCallBackParam param) {
        WxPayNotifyParam wxPayNotifyParam = new WxPayNotifyParam();
        wxPayNotifyParam.setBusinessOrderSn(param.getBusinessOrderSn());
        notifyServer.wxPayCallBack(wxPayNotifyParam);
        return ResponseData.success();
    }
}
