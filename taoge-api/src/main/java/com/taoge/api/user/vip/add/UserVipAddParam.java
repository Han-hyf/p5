package com.taoge.api.user.vip.add;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserVipAddParam extends BaseParam {
    @NotNull
    private String vipName;
    @NotNull
    private BigDecimal vipPrice;
    @NotNull
    private String vipIcon;
    @NotNull
    private Integer vipDays;
    @NotNull
    private String vipDaysName;
    @NotNull
    private Integer vipLevel;
}
