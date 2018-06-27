package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class ProtocolErrorException extends BigSqlException {
    public static final int DEFAULT_CODE = 301;


    public ProtocolErrorException(String message) {
        super(DEFAULT_CODE , message);
    }

    public ProtocolErrorException(int code, String message) {
        super(code, message);
    }

    public ProtocolErrorException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ProtocolErrorException(int code, Throwable cause) {
        super(code, cause);
    }

    public ProtocolErrorException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
