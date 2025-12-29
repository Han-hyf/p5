/*
 * @ClassName UserInfo
 * @Description 
 * @version 1.0
 * @Date 2023-11-05 21:35:26
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private String nickname;
    private Integer gender;
    private Date birthday;
    private String interest;
    private String avatar;
    private Date createTime;
    private Date updateTime;
}