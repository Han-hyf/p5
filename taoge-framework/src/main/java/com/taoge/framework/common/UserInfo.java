package com.taoge.framework.common;

import lombok.Data;

@Data
public class UserInfo extends BaseToken {
    private String appVersion;
    private String source;
    private boolean guest;
    private String loginDate;

    public boolean isGuest() {
        return userId > 2000000000000000L;
    }

    public boolean isH5() {
        return "h5".equalsIgnoreCase(source);
    }

    public boolean isApp() {
        return "ios".equalsIgnoreCase(source) || "android".equalsIgnoreCase(source);
    }

}
