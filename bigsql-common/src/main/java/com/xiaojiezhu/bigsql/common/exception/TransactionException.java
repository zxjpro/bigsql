package com.xiaojiezhu.bigsql.common.exception;

/**
 * time 2018/6/28 10:22
 *
 * @author xiaojie.zhu <br>
 */
public class TransactionException extends BigSqlException {

    public static final int DEFAULT_CODE = 305;

    public TransactionException(String message) {
        super(DEFAULT_CODE,message);
    }

    public TransactionException(int code, String message) {
        super(code, message);
    }

    public TransactionException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(int code, Throwable cause) {
        super(code, cause);
    }

    public TransactionException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
