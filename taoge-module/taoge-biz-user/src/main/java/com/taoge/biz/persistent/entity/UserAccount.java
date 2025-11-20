/*
 * @ClassName UserAccount
 * @Description 
 * @version 1.0
 * @Date 2025-11-06 17:52:35
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserAccount {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private BigDecimal totalRecharge;
    private BigDecimal totalConsume;
    private BigDecimal balance;
    private BigDecimal freeze;
    private Integer totalEarnPoint;
    private Integer totalConsumePoint;
    private Integer pointBalance;
    private Integer pointFreeze;
    private Boolean status;
    private Date createTime;
    private Date updateTime;
}