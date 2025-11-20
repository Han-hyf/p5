package com.taoge.framework.util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Http header工具类
 */
public class HeaderUtil {

    /**
     * 从header 中获取一条数据
     *
     * @param request
     * @param cname
     * @return
     */
    public static String getHeaderValue(HttpServletRequest request, String cname) {
        ArrayList<String> headers = Collections.list(request.getHeaderNames());
        if (headers.size() > 0) {
            for (String headerName : headers) {
                if (headerName.equals(cname)) {
                    return request.getHeader(cname);
                }
            }
        }
        return null;
    }
}
