package com.taoge.biz.common.enums;

/**
 * 短信业务枚举
 */
public enum SmsActionType {
    // 注册、登录、找回密码
    REGISTER(999, 300 * 1000L),
    LOGIN(999, 300 * 1000L),
    FORGET_PASSWORD(999, 360 * 1000L);

    /**
     * 每天发送条数限制
     */
    private final int maxCountByDay;

    private final long expireTime;

    SmsActionType(int maxCountByDay, long expireTime) {
        this.maxCountByDay = maxCountByDay;
        this.expireTime = expireTime;
    }

    public int getMaxCountByDay() {
        return maxCountByDay;
    }

    public long getExpireTime() {
        return expireTime;
    }
}
