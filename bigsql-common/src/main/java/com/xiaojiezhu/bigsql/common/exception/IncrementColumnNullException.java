package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class IncrementColumnNullException extends BigSqlException {
    public static final int DEFAULT_CODE = 201;

    public IncrementColumnNullException(String message) {
        super(DEFAULT_CODE,message);
    }

    public IncrementColumnNullException(int code, String message) {
        super(code, message);
    }

    public IncrementColumnNullException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public IncrementColumnNullException(int code, Throwable cause) {
        super(code, cause);
    }

    public IncrementColumnNullException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
