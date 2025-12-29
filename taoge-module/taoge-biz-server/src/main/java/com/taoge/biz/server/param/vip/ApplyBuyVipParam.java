package com.taoge.biz.server.param.vip;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class ApplyBuyVipParam extends BaseParam {
    @NotNull
    private Long userVipConfigId;
}
