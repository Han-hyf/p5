package com.taoge.framework.common;

/**
 * Created by xuejingtao
 */
public class ErrorCode {
    //成功
    public static final int SUCCESS_CODE = 200;
    public static final String SUCCESS_MSG = "SUCCESS";

    //失败
    public static final int ERROR_CODE = 500;
    public static final String ERROR_MSG = "服务器内部错误，请重试或联系客服";

    // 数据库操作异常
    public static final int DATA_ERROR_CODE = 501;
    public static final String DATA_ERROR_MSG = "数据库操作异常";

    public static final int LOGIN_TIME_OUT_CODE = 502;
    public static final String LOGIN_TIME_OUT_MSG = "登录超时";

    public static final int NOT_LOGIN_CODE = 503;
    public static final String NOT_LOGIN_MSG = "请先登录";

    public static final int ROLE_ERROR_CODE = 504;
    public static final String ROLE_ERROR_MSG = "很抱歉,您的权限不足";

    // 参数验证失败
    public static final int PARAM_VALIDATE_FAIL = 600;
    // 业务异常
    public static final int BUSINESS_VALIDATE_FAIL = 610;

    public static final int USER_INVALID_CODE = 701;
    public static final String USER_INVALID_MSG = "抱歉,用户已失效";
}
