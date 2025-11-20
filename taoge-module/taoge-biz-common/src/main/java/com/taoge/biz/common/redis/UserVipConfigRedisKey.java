package com.taoge.biz.common.redis;

public class UserVipConfigRedisKey {

    private static final String prefix = "user:vipConfig:";

    public static String getUserVipConfigListKey(){
        return prefix + "list";
    }

    public static String getUserVipConfigDetailKey(Long id){
        return prefix + "detail:";
    }
}

