/*
 * @ClassName UserVip
 * @Description 
 * @version 1.0
 * @Date 2023-11-06 21:28:21
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import lombok.Data;

import java.util.Date;

@Data
public class UserVip {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private Integer level;
    private String levelName;
    private String type;
    private Date expireTime;
    private Date createTime;
    private Date updateTime;
}