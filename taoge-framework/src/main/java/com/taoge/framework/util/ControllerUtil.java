package com.taoge.framework.util;


import com.taoge.framework.constantsEnum.TokenTypeEnum;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuejingtao
 */
public class ControllerUtil {

    public static void setToken(String token, TokenTypeEnum tokenType, HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenType.getKey(), token);
        cookie.setPath("/");
        cookie.setDomain(tokenType.getDomain());
        cookie.setHttpOnly(true);
        // 设置过期时间，通常可以很长（例如：登录用户1个月，游客1年）
        response.addCookie(cookie);
    }

    public static void cleanToken( TokenTypeEnum tokenType, HttpServletResponse response) {
        Cookie cookie = new Cookie(tokenType.getKey(), "");
        cookie.setPath("/");
        cookie.setDomain(tokenType.getDomain());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
