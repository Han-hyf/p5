package com.taoge.framework.util;

import com.taoge.framework.common.UserInfo;

public class UserContext {
    public static ThreadLocal<UserInfo> userInfoThreadLocal = new ThreadLocal<>();

    public static void set(UserInfo userInfo) {
        userInfoThreadLocal.set(userInfo);
    }

    public static UserInfo get() {
        return userInfoThreadLocal.get();
    }

    public static void remove() {
        userInfoThreadLocal.remove();
    }
}
