package com.xiaojiezhu.bigsql.common.exception;

/**
 * parse sql exception
 * @author xiaojie.zhu
 */
public class SqlParserException extends BigSqlException{
    public static final int DEFAULT_CODE = 204;


    public SqlParserException(String message) {
        super(DEFAULT_CODE,message);
    }

    public SqlParserException(int code, String message) {
        super(code, message);
    }

    public SqlParserException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
    public SqlParserException(String message, Throwable cause) {
        super(DEFAULT_CODE, message, cause);
    }

    public SqlParserException(int code, Throwable cause) {
        super(code, cause);
    }

    public SqlParserException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
