/*
 * @ClassName Order
 * @Description 
 * @version 1.0
 * @Date 2023-10-29 18:10:50
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class Order {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private String orderNo;
    private BigDecimal totalPrice;
    private BigDecimal goodsPrice;
    private BigDecimal payPrice;
    private Integer goodsCount;
    private Date payTime;
    private Date payDate;
    private Date payExpiredTime;
    private String orderStatus;
    private Date createTime;
    private Date updateTime;
}