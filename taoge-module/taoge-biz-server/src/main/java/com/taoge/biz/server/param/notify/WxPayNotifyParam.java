package com.taoge.biz.server.param.notify;

import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class WxPayNotifyParam extends BaseParam {

    private String wxOrderSn;
    private String businessOrderSn;
}
