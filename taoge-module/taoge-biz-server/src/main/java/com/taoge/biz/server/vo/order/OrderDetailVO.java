package com.taoge.biz.server.vo.order;

import com.taoge.framework.controller.BaseVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetailVO extends BaseVO {
    private String orderNo;
    private BigDecimal totalPrice;
    private BigDecimal goodsPrice;
    private BigDecimal payPrice;
    private Integer goodsCount;
    private Date payTime;
    private Date payExpiredTime;
    private String orderStatus;
    private Date createTime;
    private Date updateTime;

}
