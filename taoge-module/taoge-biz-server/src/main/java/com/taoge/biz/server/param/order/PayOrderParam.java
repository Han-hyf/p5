package com.taoge.biz.server.param.order;

import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class PayOrderParam extends BaseParam {
    private String businessOrderSn;
}
