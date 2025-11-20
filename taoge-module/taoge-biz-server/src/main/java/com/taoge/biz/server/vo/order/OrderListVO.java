package com.taoge.biz.server.vo.order;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderListVO extends BaseVO {
    private String orderNo;
    private BigDecimal totalPrice;
    private BigDecimal payPrice;
    private Date payTime;
    private String orderStatus;
    private Date createTime;
}
