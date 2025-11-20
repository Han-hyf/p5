package com.taoge.framework.exception;

/**
 * 数据库操作异常
 * Created by xuejingtao
 */
public class DatabaseException extends BaseException {

    public DatabaseException(int code) {
        super(code);
    }

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(int code, String message) {
        super(code, message);
    }
}
