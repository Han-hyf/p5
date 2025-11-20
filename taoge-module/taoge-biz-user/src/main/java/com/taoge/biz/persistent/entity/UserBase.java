/*
 * @ClassName UserBase
 * @Description 
 * @version 1.0
 * @Date 2023-11-05 21:35:25
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.util.Date;

@Data
public class UserBase {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private String username;
    private String password;
    private String salt;
    private String mobile;
    private String wxOpenId;
    private String wxUnionId;
    private String status;
    private String authStatus;
    private String authType;
    private Date registerTime;
    private Date lastLoginTime;
    private Date createTime;
    private Date updateTime;
}