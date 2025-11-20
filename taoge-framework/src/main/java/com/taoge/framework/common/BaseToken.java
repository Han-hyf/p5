package com.taoge.framework.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 各业务token基础类
 */
@Data
public class BaseToken implements Serializable {
    /**
     * 用户token
     */
    protected String token;
    /**
     * 用户id
     */
    protected Long userId;
    /**
     * 过期时间，时间戳
     */
    @JsonIgnore
    protected Long expireTime;
    /**
     * 用户类型
     */
    protected String userType;
}
