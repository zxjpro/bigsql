package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class DataSourceNotExistsException extends BigSqlException {
    public static final int DEFAULT_CODE = 104;

    public DataSourceNotExistsException(String message) {
        super(DEFAULT_CODE,message);
    }

    public DataSourceNotExistsException(int code, String message) {
        super(code, message);
    }

    public DataSourceNotExistsException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public DataSourceNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceNotExistsException(int code, Throwable cause) {
        super(code, cause);
    }

    public DataSourceNotExistsException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
