package com.taoge.api.user.vip.apply;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class UserVipApplyParam extends BaseParam {
    @NotNull
    private Long userVipConfigId;
}
