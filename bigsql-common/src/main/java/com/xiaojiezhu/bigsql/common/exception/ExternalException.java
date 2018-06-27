package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class ExternalException extends BigSqlException{

    public static final int DEFAULT_CODE = 400;

    public ExternalException(String message) {
        super(message);
    }

    public ExternalException(int code, String message) {
        super(code, message);
    }

    public ExternalException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ExternalException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_CODE;
    }

    public ExternalException(int code, Throwable cause) {
        super(code, cause);
    }

    public ExternalException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
