package com.taoge.framework.constantsEnum;

/**
 * token类型
 */
public enum TokenTypeEnum {
    TOKEN("token", "www.taogeedu.com"), // 用户token
    MERCHANT_TOKEN("m_token", "cms.taogeedu.com"), // 商户token
    SYS_TOKEN("sys_token", "sys.taogeedu.com"), // 系统用户token
    ;
    private final String key;
    private final String domain;

    TokenTypeEnum(String key, String domain) {
        this.key = key;
        this.domain = domain;
    }

    public String getKey() {
        return key;
    }

    public String getDomain() {
        return domain;
    }
}
