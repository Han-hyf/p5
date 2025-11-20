package com.taoge.framework.util;

import com.taoge.framework.common.UserInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.util.StringUtils;

public class TokenUtil {
    private static final long GUEST_TOKEN_EXPIRE_TIME = 2592000000L;

    private static final long USER_TOKEN_EXPIRE_TIME = 25920000000L;

    public static UserInfo generateToken(Long userId) {
        UserInfo userInfo = new UserInfo();
        if (null == userId) {
            userId = getRandomLong();
            userInfo.setGuest(true);
            userInfo.setExpireTime(System.currentTimeMillis() + GUEST_TOKEN_EXPIRE_TIME);
        } else {
            userInfo.setExpireTime(System.currentTimeMillis() + USER_TOKEN_EXPIRE_TIME);
        }
        StringBuilder tokenKey = new StringBuilder();
        String loginDate = format(new Date(), "YYYY-MM-dd HH:mm:ss");
        tokenKey.append(userId).append(",").append(loginDate).append(",").append(userInfo.getExpireTime());
        userInfo.setUserId(userId);
        userInfo.setLoginDate(loginDate);
        // 生成token方法，将 userId,loginDate,expireTime 这个格式的字符串，对称加密
        String tokenStr = AESUtil.AESEncode(tokenKey.toString());
        userInfo.setToken(tokenStr);
        return userInfo;
    }

    public static Long getRandomLong() {
        long num = (long)((Math.random() * 9.0D + 1.0D) * 1000.0D);
        return Long.parseLong(format(new Date(), "yyMMddHHmmss") + num);
    }

    public static String format(Date date, String format) {
        if (date == null)
            return "";
        return (new SimpleDateFormat(format)).format(date);
    }

    public static UserInfo getToken(String tokenAES) {
        if (StringUtils.isEmpty(tokenAES))
            return null;
        try {
            String[] tokenStr = AESUtil.AESDecode(tokenAES).split(",");
            UserInfo token = new UserInfo();
            token.setUserId(Long.parseLong(tokenStr[0]));
            if (tokenStr.length > 1)
                token.setLoginDate(tokenStr[1]);
            if (tokenStr.length > 2)
                token.setExpireTime(Long.parseLong(tokenStr[2]));
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
