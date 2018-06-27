package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class DatabaseNotFoundException extends BigSqlException {

    public static final int DEFAULT_CODE = 106;

    public DatabaseNotFoundException(String message) {
        super(message);
    }

    public DatabaseNotFoundException(int code, String message) {
        super(code, message);
    }

    public DatabaseNotFoundException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public DatabaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseNotFoundException(int code, Throwable cause) {
        super(code, cause);
    }

    public DatabaseNotFoundException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
