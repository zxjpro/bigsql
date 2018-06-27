package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class ShardingColumnNotExistException extends BigSqlException {
    public static final int DEFAULT_CODE = 203;

    public ShardingColumnNotExistException(String message) {
        super(DEFAULT_CODE,message);
    }

    public ShardingColumnNotExistException(int code, String message) {
        super(code, message);
    }

    public ShardingColumnNotExistException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ShardingColumnNotExistException(int code, Throwable cause) {
        super(code, cause);
    }

    public ShardingColumnNotExistException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
