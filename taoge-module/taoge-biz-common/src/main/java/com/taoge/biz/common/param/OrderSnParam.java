package com.taoge.biz.common.param;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class OrderSnParam extends BaseParam {
    @NotNull
    private String orderSn;
}
