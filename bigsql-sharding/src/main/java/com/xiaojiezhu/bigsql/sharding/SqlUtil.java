package com.xiaojiezhu.bigsql.sharding;

/**
 * @author xiaojie.zhu
 */
public class SqlUtil {

    public static String updateTableName(String sql,String oldTableName,String newTableName){
        String nsql = sql.replaceAll(oldTableName,newTableName);
        return nsql;
    }
}
