package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class NotSupportException extends BigSqlException {
    public static final int DEFAULT_CODE = 303;

    public NotSupportException(String message) {
        super(DEFAULT_CODE,message);
    }

    public NotSupportException(int code, String message) {
        super(code, message);
    }

    public NotSupportException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public NotSupportException(int code, Throwable cause) {
        super(code, cause);
    }

    public NotSupportException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
