package com.taoge.biz.server.param.order;

import com.taoge.biz.common.enums.BusinessTypeEnum;
import com.taoge.framework.controller.BaseParam;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplyBusinessOrderParam extends BaseParam {
    private Long userId;
    private BusinessTypeEnum businessType;
    private String businessParam;
    private BigDecimal totalMoney;
    private BigDecimal payMoney;
}
