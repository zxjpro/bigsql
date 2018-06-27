package com.xiaojiezhu.bigsql.sharding;

/**
 * the execute block, execute a sql on a connection
 * @author xiaojie.zhu
 */
public class ExecuteBlock {

    private String dataSourceName;
    private String sql;

    public ExecuteBlock() {
    }

    public ExecuteBlock(String dataSourceName, String sql) {
        this.dataSourceName = dataSourceName;
        this.sql = sql;
    }


    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql + " : " + dataSourceName;
    }
}
