package com.taoge.api.common.redis;

import com.taoge.biz.common.enums.SmsActionType;
import org.springframework.stereotype.Component;

@Component
public class VerifyCodeRedisKey {

    private static final String prefix = "rest:code:";


    /**
     * 生成短信验证码标记的key
     */
    public static String getValidateCodeMarkKey(Long userId, String originMobile, SmsActionType register) {
        return prefix + userId + ":" + originMobile + ":" + register.name();

    }
}
