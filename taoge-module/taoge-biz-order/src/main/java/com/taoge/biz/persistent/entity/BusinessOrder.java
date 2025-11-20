/*
 * @ClassName BusinessOrder
 * @Description 
 * @version 1.0
 * @Date 2025-11-10 14:03:16
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BusinessOrder {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private String businessOrderSn;
    private Long userId;
    private String signKey;
    private BigDecimal totalMoney;
    private BigDecimal payMoney;
    private String payType;
    private String businessType;
    private String businessParam;
    private String status;
    private Date payTime;
    private Date payExpireTime;
    private Date createTime;
    private Date updateTime;
}