package com.xiaojiezhu.bigsql.model.constant;

/**
 * @author xiaojie.zhu<br>
 * 时间 2018/5/3 16:30
 * 说明 https://dev.mysql.com/doc/internals/en/com-query-response.html#column-type
 */
public enum  ColumnType {
    DECIMAL(0x00),
    TINY(0x01),
    SHORT(0x02),
    LONG(0x03),

    BIGINT(-5),
    TINYINT(-6),

    FLOAT(0x04),
    DOUBLE(0x05),
    NULL(0x06),
    TIMESTAMP(0x07),
    LONGLONG(0x08),
    //int(24)
    INT(0x09),
    DATE(0x0a),
    TIME(0x0b),
    DATETIME(0x0c),
    YEAR(0x0d),
    NEWDATE(0x0e),
    VARCHAR(0x0f),
    BIT(0x10),
    TIMESTAMP2(0x11),
    DATETIME2(0x12),
    TIME2(0x13),
    NEWDECIMAL(0xf6),
    ENUM(0xf7),
    SET(0xf8),
    TINY_BLOB(0xf9),
    MEDIUM_BLOB(0xfa),
    LONG_BLOB(0xfb),
    BLOB(0xfc),
    VAR_STRING(0xfd),
    STRING(0xfe),
    GEOMETRY(0xff);

    private int value;

    ColumnType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }


}
