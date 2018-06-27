package com.xiaojiezhu.bigsql.common.exception;

import com.xiaojiezhu.bigsql.model.constant.Constant;

/**
 * @author xiaojie.zhu
 */
public class BigSqlException extends RuntimeException {
    public static final int DEFAULT_CODE = 10;
    protected int code;

    public BigSqlException(String message) {
        this(DEFAULT_CODE,message);
    }

    public BigSqlException(int code, String message) {
        super("code:" + code + " , " + message);
        this.code = code;
    }

    public BigSqlException(int code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BigSqlException(String message, Throwable cause) {
        super(message, cause);
        this.code = DEFAULT_CODE;

    }

    public BigSqlException(int code , Throwable cause) {
        super(cause);
        this.code = code;
    }

    public BigSqlException(int code , String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return Constant.SERVER_INFO + " , " +super.getMessage();
    }
}
