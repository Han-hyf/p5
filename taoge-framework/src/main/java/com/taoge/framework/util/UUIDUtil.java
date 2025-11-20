package com.taoge.framework.util;

import java.util.UUID;

/**
 * UUID工具类
 * <p>
 * Created by xuejingtao
 */
public class UUIDUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
