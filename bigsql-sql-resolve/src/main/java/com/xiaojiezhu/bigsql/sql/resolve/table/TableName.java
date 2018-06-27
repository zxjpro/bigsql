package com.xiaojiezhu.bigsql.sql.resolve.table;

/**
 * @author xiaojie.zhu
 */
public interface TableName {

    /**
     * get table name
     * @return the real table name
     */
    String getTableName();

    /**
     * get the alias table name
     * @return alias table name
     */
    String getAsTableName();
}
