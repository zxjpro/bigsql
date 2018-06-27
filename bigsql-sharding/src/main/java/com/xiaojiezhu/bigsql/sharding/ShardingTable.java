package com.xiaojiezhu.bigsql.sharding;

/**
 * @author xiaojie.zhu
 */
public class ShardingTable {

    private String tableName;

    private String dataSourceName;


    public ShardingTable(String tableName, String dataSourceName) {
        this.tableName = tableName;
        this.dataSourceName = dataSourceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    @Override
    public String toString() {
        return tableName + " : " + dataSourceName;
    }
}
