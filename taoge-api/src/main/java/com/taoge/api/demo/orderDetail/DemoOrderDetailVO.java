package com.taoge.api.demo.orderDetail;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DemoOrderDetailVO extends BaseVO {
    private String orderNo;
    private BigDecimal totalPrice;
    private BigDecimal goodsPrice;
    private BigDecimal payPrice;
    private Integer goodsCount;
    private Date payTime;
    private String orderStatus;

}
