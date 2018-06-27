package com.xiaojiezhu.bigsql.core;

import com.xiaojiezhu.bigsql.model.construct.Field;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class SimpleResultSet {
    private List<Field> fields;
    private List<Object[]> rowData;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Object[]> getRowData() {
        return rowData;
    }

    public void setRowData(List<Object[]> rowData) {
        this.rowData = rowData;
    }
}
