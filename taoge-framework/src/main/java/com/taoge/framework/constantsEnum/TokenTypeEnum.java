package com.taoge.framework.constantsEnum;

/**
 * token类型
 */
public enum TokenTypeEnum {
    TOKEN("t"), // 用户token
    ANCHOR_TOKEN("t"), // 主播token
    ASSISTANT_TOKEN("t"), // 助理token
    SYS_TOKEN("sys_t"), // 平台管理员token
    MERCHANT_TOKEN("merchant_t"), // 商户token
    ;
    private String key;

    TokenTypeEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
