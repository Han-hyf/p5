package com.taoge.biz.common.redis;

import com.taoge.biz.common.enums.SmsActionType;


/**
 * 短信redis的key
 */
public class SmsRedisKey {
    //一般key = prefix + key
    private static final String prefix = "sms:";



    /**
     * 查询当天条件下短信数量
     * @param identity
     * @param actionType
     * @param day
     * @return
     */
    public static String getSmsCountInDayKey(String identity, SmsActionType actionType,String day){
        return prefix + "count:" + day + ":" + actionType.name() + ":" + identity;
    }

    /**
     * 查询总量
     */
    public static String getSmsTotalInDay(String day){
        return prefix + "total:" + day;
    }

    public static String getSendLockKey(String key){

        return prefix + "lock:" + key;

    }


}
