/*
 * @ClassName UserAuth
 * @Description 
 * @version 1.0
 * @Date 2023-11-05 21:35:25
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.util.Date;

@Data
public class UserAuth {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private String idCardFront;
    private String idCardBack;
    private String idCardNo;
    private String realName;
    private String idCardFrontStatus;
    private String idCardBackStatus;
    private String authStatus;
    private String reason;
    private Date authTime;
    private Date createTime;
    private Date updateTime;
}