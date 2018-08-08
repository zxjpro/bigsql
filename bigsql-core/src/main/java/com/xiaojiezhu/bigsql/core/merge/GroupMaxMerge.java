package com.xiaojiezhu.bigsql.core.merge;

import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;

import java.sql.ResultSet;
import java.util.List;

/**
 * time 2018/8/8 15:47
 * @author xiaojie.zhu <br>
 */
public class GroupMaxMerge extends GroupByMerge {

    public GroupMaxMerge(String databaseName, String tableName, List<ResultSet> resultSets, AliasField.FunctionType functionType, int functionIndex, int groupFieldIndex) {
        super(databaseName, tableName, resultSets, functionType, functionIndex, groupFieldIndex);
    }

    @Override
    protected <T> T reduce(T t1, T t2) {
        if(isInteger(t2)){
            long l1 = Long.parseLong(String.valueOf(t1));
            long l2 = Long.parseLong(String.valueOf(t2));
            Long max = Long.max(l1,l2);
            return (T) max;
        }else{
            double d1 = Double.parseDouble(String.valueOf(t1));
            double d2 = Double.parseDouble(String.valueOf(t2));

            Double max = Double.max(d1,d2);
            return (T) max;
        }
    }
}
