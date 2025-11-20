package com.taoge.biz.common.constant;

import com.taoge.biz.common.enums.SmsActionType;

import java.util.HashMap;

public class SmsConstant {

    private static final HashMap<SmsActionType,String> templateCodeMap = new HashMap<>();
    static {
        templateCodeMap.put(SmsActionType.REGISTER,"1");
        templateCodeMap.put(SmsActionType.LOGIN,"2");
        templateCodeMap.put(SmsActionType.FORGET_PASSWORD,"3");
    }

    public static String getSmsTemplateCode(SmsActionType actionType){
        return templateCodeMap.get(actionType);
    }
}
