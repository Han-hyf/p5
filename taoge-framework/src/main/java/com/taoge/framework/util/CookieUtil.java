package com.taoge.framework.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie工具类
 */
public class CookieUtil {
    /**
     * 设置Cookie 无有效期
     * @param response
     * @param cname
     * @param value
     * @param domain
     * @param httpOnly
     */
    public static void setCookie(HttpServletResponse response, String cname, String value, String domain, Boolean httpOnly) {
        Cookie cookie = new Cookie(cname, value);
        cookie.setPath("/");
        if(StringUtils.isNotEmpty(domain))
            cookie.setDomain(domain);
        if(httpOnly != null)
            cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    /**
     * 获取key=cname的Cookie
     * @param request
     * @param cname
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request, String cname){
        Cookie cookies[] = request.getCookies();
        Cookie c = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                c = cookies[i];
                if(c.getName().equals(cname)){
                    return c;
                }
            }
        }
        return null;
    }

    public static String getCookieByName(HttpServletRequest request, String name) {
        if (null != request.getCookies()) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取key=cname的Cookie值
     * @param request
     * @param cname
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cname){
        Cookie c = getCookie(request, cname);
        return c == null ? "" : c.getValue();
    }

    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String ip_ = ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
        if (ip_.contains(",")) {
            ip = ip_.split(",")[0];
        }
        return ip;
    }


}
