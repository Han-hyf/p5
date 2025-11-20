package com.taoge.api.demo.orderDetail;

import com.taoge.framework.annotation.NotNull;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

@Data
public class DemoOrderDetailParam extends BaseParam {
    @NotNull(errorMsg = "请选择要查看的订单")
    private Long orderId;
}
