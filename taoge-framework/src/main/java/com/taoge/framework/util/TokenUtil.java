package com.taoge.framework.util;

import com.taoge.framework.common.MerchantInfo;
import com.taoge.framework.common.SysUserInfo;
import com.taoge.framework.common.UserInfo;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.taoge.framework.constantsEnum.TokenTypeEnum;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {
    private static final long GUEST_TOKEN_EXPIRE_TIME = 2592000000L;

    private static final long USER_TOKEN_EXPIRE_TIME = 259200000L;

    /**
     * 商户token过期时间1天
     */
    private static final long MERCHANT_TOKEN_EXPIRE_TIME = 86400000L;
    /**
     * 系统用户token过期时间
     */
    private static final long SYS_USER_TOKEN_EXPIRE_TIME = 86400000L;

    public static UserInfo generateUserToken(Long userId) {
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

    /**
     * 生成商户token
     *
     * @param merchantId 商户id
     */
    public static MerchantInfo generateMerchantToken(Long merchantId) {
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setExpireTime(System.currentTimeMillis() + MERCHANT_TOKEN_EXPIRE_TIME);
        StringBuilder tokenKey = new StringBuilder();
        String loginDate = format(new Date(), "YYYY-MM-dd HH:mm:ss");
        tokenKey.append(merchantId).append(",").append(loginDate).append(",").append(merchantInfo.getExpireTime());
        merchantInfo.setUserId(merchantId);
        merchantInfo.setLoginDate(loginDate);
        // 生成token方法，将 userId,loginDate,expireTime 这个格式的字符串，对称加密
        String tokenStr = AESUtil.AESEncode(tokenKey.toString());
        merchantInfo.setToken(tokenStr);
        return merchantInfo;
    }

    /**
     * 生成系统用户token
     *
     * @param merchantId 商户id
     */
    public static SysUserInfo generateSysUserInfoToken(Long merchantId) {
        SysUserInfo sysUserInfo = new SysUserInfo();
        sysUserInfo.setExpireTime(System.currentTimeMillis() + SYS_USER_TOKEN_EXPIRE_TIME);
        StringBuilder tokenKey = new StringBuilder();
        String loginDate = format(new Date(), "YYYY-MM-dd HH:mm:ss");
        tokenKey.append(merchantId).append(",").append(loginDate).append(",").append(sysUserInfo.getExpireTime());
        sysUserInfo.setUserId(merchantId);
        sysUserInfo.setLoginDate(loginDate);
        // 生成token方法，将 userId,loginDate,expireTime 这个格式的字符串，对称加密
        String tokenStr = AESUtil.AESEncode(tokenKey.toString());
        sysUserInfo.setToken(tokenStr);
        return sysUserInfo;
    }

    public static Long getRandomLong() {
        long num = (long) ((Math.random() * 9.0D + 1.0D) * 1000.0D);
        return Long.parseLong(format(new Date(), "yyMMddHHmmss") + num);
    }

    public static String format(Date date, String format) {
        if (date == null)
            return "";
        return (new SimpleDateFormat(format)).format(date);
    }

    public static UserInfo analysisUserToken(String tokenAES) {
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

    public static MerchantInfo analysisMerchantToken(String tokenAES) {
        if (StringUtils.isEmpty(tokenAES))
            return null;
        try {
            String[] tokenStr = AESUtil.AESDecode(tokenAES).split(",");
            MerchantInfo token = new MerchantInfo();
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

    public static SysUserInfo analysisSysUserInfoToken(String tokenAES) {
        if (StringUtils.isEmpty(tokenAES))
            return null;
        try {
            String[] tokenStr = AESUtil.AESDecode(tokenAES).split(",");
            SysUserInfo token = new SysUserInfo();
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

    public static String getToken(TokenTypeEnum typeEnum, HttpServletRequest request) {
        String token = request.getHeader(typeEnum.getKey());
        if (org.apache.commons.lang3.StringUtils.isEmpty(token)) {
            token = CookieUtil.getCookieByName(request, typeEnum.getKey());
            if (null != token) {
                token = URLDecoder.decode(token, StandardCharsets.UTF_8);
            }
        }
        return token;
    }
}
