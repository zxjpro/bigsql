package com.xiaojiezhu.bigsql.sharding;

import com.xiaojiezhu.bigsql.common.SqlConstant;

/**
 * @author xiaojie.zhu
 */
public class SqlUtil {


    public static String updateTableName(String sql,String oldTableName,String newTableName){
        String nsql = sql.replaceAll(oldTableName,newTableName);
        return nsql;
    }

    public static String realName(String name) {
        if(name.startsWith(SqlConstant.FIELD_ALOUD)){
            name = name.replaceAll(SqlConstant.FIELD_ALOUD,"");
        }
        return name;
    }
}
