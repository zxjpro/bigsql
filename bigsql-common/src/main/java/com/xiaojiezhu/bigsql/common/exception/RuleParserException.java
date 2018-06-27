package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class RuleParserException extends BigSqlException {
    public static final int DEFAULT_CODE = 102;

    public RuleParserException(String message) {
        super(DEFAULT_CODE ,message);
    }

    public RuleParserException(int code, String message) {
        super(code, message);
    }

    public RuleParserException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public RuleParserException(int code, Throwable cause) {
        super(code, cause);
    }

    public RuleParserException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
