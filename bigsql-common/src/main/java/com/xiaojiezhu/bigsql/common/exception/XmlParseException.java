package com.xiaojiezhu.bigsql.common.exception;

/**
 * @author xiaojie.zhu
 */
public class XmlParseException extends BigSqlException {

    public static final int DEFAULT_CODE = 103;

    public XmlParseException(String message) {
        super(DEFAULT_CODE , message);
    }

    public XmlParseException(int code, String message) {
        super(code, message);
    }

    public XmlParseException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public XmlParseException(int code, Throwable cause) {
        super(code, cause);
    }

    public XmlParseException(int code, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(code, message, cause, enableSuppression, writableStackTrace);
    }
}
