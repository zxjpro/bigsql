package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class DataSourceLoadException extends BigSqlException {
    public static final int DEFAULT_CODE = 101;

    public DataSourceLoadException(String message) {
        super(DEFAULT_CODE,message);
    }

    public DataSourceLoadException(int code, String message) {
        super(code, message);
    }

    public DataSourceLoadException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public DataSourceLoadException(int code, Throwable cause) {
        super(code, cause);
    }

    public DataSourceLoadException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
