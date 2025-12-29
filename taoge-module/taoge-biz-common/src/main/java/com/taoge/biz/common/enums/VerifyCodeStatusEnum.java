package com.taoge.biz.common.enums;

/**
 * 验证码状态枚举
 */
public enum VerifyCodeStatusEnum {
    INIT, //等待验证
    SUCCESS, //验证通过
    WRONG, //错误
    FAIL, //失败
    INVALID, //失效
}
