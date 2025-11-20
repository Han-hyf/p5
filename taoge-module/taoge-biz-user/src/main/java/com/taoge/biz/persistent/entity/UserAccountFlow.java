/*
 * @ClassName UserAccountFlow
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
public class UserAccountFlow {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private BigDecimal amount;
    private String businessType;
    private String businessParam;
    private String identity;
    private String remark;
    private Date flowTime;
    private Date createTime;
    private Date updateTime;
}