package com.xiaojiezhu.bigsql.core.configuration;

import com.xiaojiezhu.bigsql.model.constant.ColumnType;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/22 16:29
 * 说明 ...
 */
public class Entry {
    private ColumnType columnType;

    private Object value;

    public Entry(ColumnType columnType, Object value) {
        this.columnType = columnType;
        this.value = value;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
