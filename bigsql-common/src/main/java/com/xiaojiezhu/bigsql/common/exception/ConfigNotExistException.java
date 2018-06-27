package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class ConfigNotExistException extends BigSqlException {
    public static final int DEFAULT_CODE = 105;


    public ConfigNotExistException(String message) {
        super(DEFAULT_CODE , message);
    }

    public ConfigNotExistException(int code, String message) {
        super(code, message);
    }

    public ConfigNotExistException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ConfigNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigNotExistException(int code, Throwable cause) {
        super(code, cause);
    }

    public ConfigNotExistException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
