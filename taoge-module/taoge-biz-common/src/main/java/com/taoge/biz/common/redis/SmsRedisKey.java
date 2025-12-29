package com.taoge.biz.common.redis;

import com.taoge.biz.common.enums.SmsActionType;

/**
 * 短信redis key
 */
public class SmsRedisKey {
    private static final String prefix = "sms:";

    /**
     * 查询当天短信发送总量key
     * 示例：
     * sms:count:20231101:REGISTER:1
     * sms:count:20231101:REGISTER:13712345678
     * sms:count:20231101:REGISTER:127.0.0.1
     *
     * @param identity   业务标识
     * @param actionType 业务类型
     * @param day        日期：yyyyMMdd
     */
    public static String getSmsCountInDayKey(String identity, SmsActionType actionType, String day) {
        return prefix + "count:" + day + ":" + actionType.name() + ":" + identity;
    }

    /**
     * 查询当天发送信息总量
     *
     * @param day 日期：yyyyMMdd
     */
    public static String getSmsTotalInDay(String day) {
        return prefix + "total:" + day;
    }

    /**
     * 防止重复发送锁
     */
    public static String getSendLockKey(String key) {
        return prefix + "lock:" + key;
    }

}
