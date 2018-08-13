package com.xiaojiezhu.bigsql.core.merge.groupby;

import com.xiaojiezhu.bigsql.core.merge.fn.SumMerge;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;

import java.sql.ResultSet;
import java.util.List;

/**
 * time 2018/8/8 15:47
 * @author xiaojie.zhu <br>
 */
public class GroupSumMerge extends GroupByMerge {
    public static SumMerge SUM_MERGE = new SumMerge(null,null,null);

    public GroupSumMerge(String databaseName, String tableName, List<ResultSet> resultSets, AliasField.FunctionType functionType, int functionIndex, int groupFieldIndex) {
        super(databaseName, tableName, resultSets, functionType, functionIndex, groupFieldIndex);
    }

    @Override
    protected <T> T reduce(T t1, T t2) {
        return (T) SUM_MERGE.reduce(t1,t2);
    }
}
