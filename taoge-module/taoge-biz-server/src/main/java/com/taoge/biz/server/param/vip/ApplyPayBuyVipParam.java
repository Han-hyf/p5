package com.taoge.biz.server.param.vip;

import com.taoge.biz.common.enums.PayTypeEnum;
import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class ApplyPayBuyVipParam extends BaseParam {
    @NotNull
    private String businessOrderSn;
    @NotNull
    private PayTypeEnum payType;
}
