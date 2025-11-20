/*
 * @ClassName UserVipConfig
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
public class UserVipConfig {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private String vipName;
    private BigDecimal vipPrice;
    private String vipIcon;
    private Integer vipDays;
    private String vipDaysName;
    private Integer vipLevel;
    private Boolean status;
    private Long sort;
    private Date createTime;
    private Date updateTime;
}