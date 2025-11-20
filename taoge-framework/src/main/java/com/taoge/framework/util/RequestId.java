package com.taoge.framework.util;

import org.slf4j.MDC;

/**
 * Created by Administrator on 2015/4/29.
 */
public class RequestId {

    private static final String TRACKID = "GlobalTrackId";

    public static String getRequestId() {
        String id = MDC.get(TRACKID);
        if (id == null) {
            id = Identities.uuid2();
            MDC.put(TRACKID, id);
        }
        return id;
    }

    public static void createId() {
        String id = Identities.uuid();
        MDC.put(TRACKID, id);
    }

    public static void setRequestId(String id) {
        MDC.put(TRACKID, id);
    }

    public static void clear() {
        MDC.remove(TRACKID);
    }

}


