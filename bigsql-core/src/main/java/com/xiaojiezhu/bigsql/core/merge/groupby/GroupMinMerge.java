package com.xiaojiezhu.bigsql.core.merge.groupby;

import com.xiaojiezhu.bigsql.core.merge.fn.MinMerge;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;

import java.sql.ResultSet;
import java.util.List;

/**
 * time 2018/8/8 15:47
 * @author xiaojie.zhu <br>
 */
public class GroupMinMerge extends GroupByMerge {
    public static final MinMerge MIN_MERGE = new MinMerge(null,null,null);

    public GroupMinMerge(String databaseName, String tableName, List<ResultSet> resultSets, AliasField.FunctionType functionType, int functionIndex, int groupFieldIndex) {
        super(databaseName, tableName, resultSets, functionType, functionIndex, groupFieldIndex);
    }

    @Override
    protected <T> T reduce(T t1, T t2) {
        return (T) MIN_MERGE.reduce(t1,t2);
    }
}
