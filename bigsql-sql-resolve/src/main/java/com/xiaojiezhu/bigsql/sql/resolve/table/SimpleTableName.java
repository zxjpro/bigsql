package com.xiaojiezhu.bigsql.sql.resolve.table;

/**
 * @author xiaojie.zhu
 */
public class SimpleTableName implements TableName {
    protected String tableName;
    protected String asTableName;

    public SimpleTableName(String tableName) {
        this.tableName = tableName;
    }

    public SimpleTableName(String tableName, String asTableName) {
        this.tableName = tableName;
        this.asTableName = asTableName;
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public String getAsTableName() {
        return this.asTableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setAsTableName(String asTableName) {
        this.asTableName = asTableName;
    }

    @Override
    public String toString() {
        return tableName + " as '"+ asTableName +"'";
    }
}
