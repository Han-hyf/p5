package com.taoge.biz.server.param.order;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class OrderDetailParam extends BaseParam {
    @NotNull(errorMsg = "请选择要查看的订单")
    private Long orderId;
}
