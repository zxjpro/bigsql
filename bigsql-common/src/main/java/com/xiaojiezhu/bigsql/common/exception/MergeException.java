package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class MergeException extends BigSqlException {
    public static final int DEFAULT_CODE = 304;

    public MergeException(String message) {
        super(DEFAULT_CODE,message);
    }

    public MergeException(int code, String message) {
        super(code, message);
    }

    public MergeException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public MergeException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_CODE;
    }

    public MergeException(int code, Throwable cause) {
        super(code, cause);
    }

    public MergeException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
