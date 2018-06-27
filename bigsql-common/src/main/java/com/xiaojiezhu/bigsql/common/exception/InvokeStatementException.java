package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class InvokeStatementException extends BigSqlException {
    public static final int DEFAULT_CODE = 302;

    public InvokeStatementException(String message) {
        super(DEFAULT_CODE,message);
    }

    public InvokeStatementException(int code, String message) {
        super(code, message);
    }

    public InvokeStatementException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public InvokeStatementException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    public InvokeStatementException(int code, Throwable cause) {
        super(code, cause);
    }

    public InvokeStatementException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
