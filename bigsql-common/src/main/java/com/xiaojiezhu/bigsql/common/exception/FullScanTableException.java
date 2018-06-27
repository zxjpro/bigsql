package com.xiaojiezhu.bigsql.common.exception;

/**
 * full scan table
 * @author xiaojie.zhu
 */
public class FullScanTableException extends BigSqlException {
    public static final int DEFAULT_CODE = 202;

    public FullScanTableException(String message) {
        super(DEFAULT_CODE,message);
    }

    public FullScanTableException(int code, String message) {
        super(code, message);
    }

    public FullScanTableException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public FullScanTableException(int code, Throwable cause) {
        super(code, cause);
    }

    public FullScanTableException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
