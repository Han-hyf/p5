/*
 * @ClassName UserVipRecord
 * @Description 
 * @version 1.0
 * @Date 2023-11-07 20:17:12
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserVipRecord {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private String businessOrderSn;
    private String vipName;
    private BigDecimal vipPrice;
    private String vipIcon;
    private Integer vipDays;
    private String vipDaysName;
    private Integer vipLevel;
    private BigDecimal payAmount;
    private Date payDate;
    private String status;
    private String remark;
    private Date createTime;
    private Date updateTime;
}