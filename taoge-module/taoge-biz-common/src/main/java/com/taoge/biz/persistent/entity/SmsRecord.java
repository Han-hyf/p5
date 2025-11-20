/*
 * @ClassName SmsRecord
 * @Description 
 * @version 1.0
 * @Date 2025-10-23 16:05:19
 */
package com.taoge.biz.persistent.entity;

import com.taoge.framework.annotation.PrimaryKey;
import java.util.Date;
import lombok.Data;

@Data
public class SmsRecord {
    @PrimaryKey(autoIncrement = true)
    private Long id;
    private Long userId;
    private String mobile;
    private String mobilePrefix;
    private String iso;
    private String originalMobile;
    private String actionType;
    private String content;
    private String templateCode;
    private String templateParam;
    private String ip;
    private Boolean sendStatus;
    private String sendMessage;
    private Date sendTime;
    private Date sendDay;
    private Date createTime;
    private Date updateTime;
}