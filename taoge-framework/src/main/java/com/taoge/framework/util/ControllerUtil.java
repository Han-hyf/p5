package com.taoge.framework.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuejingtao
 */
public class ControllerUtil {

    public static void setToken(String token, String domain, HttpServletResponse response) {
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setHttpOnly(true);
        // 设置过期时间，通常可以很长（例如：登录用户1个月，游客1年）
        response.addCookie(cookie);
    }

    public static void cleanToken(HttpServletResponse response, String domain) {
        Cookie cookie = new Cookie("token", "");
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
