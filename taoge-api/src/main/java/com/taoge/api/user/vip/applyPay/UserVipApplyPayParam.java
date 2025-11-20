package com.taoge.api.user.vip.applyPay;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class UserVipApplyPayParam extends BaseParam {

    @NotNull
    private String businessOrderSn;
    @NotNull
    private String payType;
}
