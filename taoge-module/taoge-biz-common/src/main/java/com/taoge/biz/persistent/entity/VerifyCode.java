/*
 * @ClassName VerifyCode
 * @Description 
 * @version 1.0
 * @Date 2023-10-31 21:36:54
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.util.Date;

@Data
public class VerifyCode {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private String mobile;
    private String code;
    private String actionType;
    private String status;
    private Integer failCount;
    private Date expireTime;
}