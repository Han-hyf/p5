/*
 * @ClassName VerifyCode
 * @Description 
 * @version 1.0
 * @Date 2025-10-23 16:05:19
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import java.util.Date;
import lombok.Data;

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