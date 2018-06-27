package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class SqlNotSupportException extends BigSqlException {
    public static final int DEFAULT_CODE = 304;

    public SqlNotSupportException(String message) {
        super(DEFAULT_CODE,message);
    }

    public SqlNotSupportException(int code, String message) {
        super(code, message);
    }

    public SqlNotSupportException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public SqlNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlNotSupportException(int code, Throwable cause) {
        super(code, cause);
    }

    public SqlNotSupportException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
