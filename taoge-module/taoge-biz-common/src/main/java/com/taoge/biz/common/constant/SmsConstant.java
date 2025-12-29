package com.taoge.biz.common.constant;

import com.taoge.biz.common.enums.SmsActionType;

import java.util.HashMap;

/**
 * 短信常量
 */
public class SmsConstant {
    // {业务类型:模板id}
    private static final HashMap<SmsActionType, String> templateCodeMap = new HashMap<>();

    // {渠道:{业务类型:模板id}}
    static {
        templateCodeMap.put(SmsActionType.REGISTER, "790370");
        templateCodeMap.put(SmsActionType.LOGIN, "790371");
        templateCodeMap.put(SmsActionType.FORGET_PASSWORD, "792583");
    }

    /**
     * 根据业务类型获取模板id
     *
     * @param actionType 业务类型
     * @return
     */
    public static String getSmsTemplateCode(SmsActionType actionType) {
        return templateCodeMap.get(actionType);
    }

    public static String[] generateTxTemplateParam(SmsActionType actionType, String code) {
        switch (actionType) {
            case LOGIN:
            case REGISTER:
                // 假如模板文案是【您的注册验证码：{1}，如非本人操作，请忽略本短信！】
                // return new String[]{code};
            case FORGET_PASSWORD:
            default:
                return new String[]{code, "3"};
        }
    }

    /**
     * 判断当前系统环境，是否是测试环境
     * 正常业务下，可以通过环境变量判断（application.yml）
     */
    public static boolean isEnvDev() {
        return true;
    }
}
