package com.taoge.biz.common.enums;

public enum SmsActionType {
    //注册，登录，找回密码，安全认证
    REGISTER(3),
    LOGIN(5),
    FORGET_PASSWORD(4);


    /**
     * 不同业务每天发送短信数量限制
     */
    private final int maxCountByDay;

    SmsActionType(int maxCountDay) {
        this.maxCountByDay = maxCountDay;
    }
    public int getMaxCountByDay() {
        return maxCountByDay;
    }
}
