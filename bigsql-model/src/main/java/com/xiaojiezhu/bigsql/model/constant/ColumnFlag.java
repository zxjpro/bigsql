package com.xiaojiezhu.bigsql.model.constant;

/**
 * @author xiaojie.zhu<br>
 * 时间 2018/5/3 16:34
 * 说明 ...
 */
public enum  ColumnFlag {

    NOT_NULL_FLAG(0x0001),
    PRI_KEY_FLAG(0x0002),
    UNIQUE_KEY_FLAG(0x0004),
    MULTIPLE_KEY_FLAG(0x0008),
    BLOB_FLAG(0x0010),
    UNSIGNED_FLAG(0x0020),
    ZEROFILL_FLAG(0x0040),
    BINARY_FLAG(0x0080),
    ENUM_FLAG(0x0100),
    AUTO_INCREMENT_FLAG(0x0200),
    TIMESTAMP_FLAG(0x0400),
    SET_FLAG(0x0800);

    private int value;


    ColumnFlag(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
