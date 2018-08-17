package com.xiaojiezhu.bigsql.core.merge;

import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.core.merge.fn.MaxMerge;
import com.xiaojiezhu.bigsql.core.merge.fn.MinMerge;
import com.xiaojiezhu.bigsql.core.merge.fn.SumMerge;
import com.xiaojiezhu.bigsql.core.merge.groupby.GroupByMerge;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SimpleField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.DefaultCommandSelectStatement;

import java.sql.ResultSet;
import java.util.List;

/**
 * time 2018/8/6 17:35
 *
 * @author xiaojie.zhu <br>
 */
public class MergeFactory {

    public static final int MAX_QUERY_FIELD_SIZE = 2;

    /**
     * get a merge
     * @param databaseName
     * @param tableName
     * @param selectStatement
     * @param resultSets
     * @return
     */
    public static Merge getMerge(String databaseName, String tableName, DefaultCommandSelectStatement selectStatement, List<ResultSet> resultSets){
        if(selectStatement.getGroupField() != null){
            //group by
            return getGroupByMerge(databaseName, tableName, selectStatement, resultSets);

        }else if(selectStatement.getOrderField() != null){
            // order by
            return new OrderByMerge(databaseName, tableName, resultSets, selectStatement);

        }else{
            List<AliasField> queryField = selectStatement.getQueryField();
            if(queryField.size() == 1 && AliasField.FieldType.FUNCTION.equals(queryField.get(0).getFieldType())){
                // function , SUM,COUNT,MAX,MIN
                AliasField aliasField = queryField.get(0);
                if(AliasField.FunctionType.COUNT == aliasField.getFunctionType() || AliasField.FunctionType.SUM == aliasField.getFunctionType()){
                    return new SumMerge(databaseName, tableName, resultSets);

                }else if(AliasField.FunctionType.MAX == aliasField.getFunctionType()){
                    return new MaxMerge(databaseName,tableName,resultSets);

                }else if(AliasField.FunctionType.MIN == aliasField.getFunctionType()){
                    return new MinMerge(databaseName, tableName, resultSets);

                }else{
                    return new ItemMerge(databaseName,tableName,resultSets);
                }
            }else{
                return new ItemMerge(databaseName,tableName,resultSets);
            }
        }
    }


    /**
     * create a group by merge
     * @param database
     * @param table
     * @param selectStatement
     * @param resultSets
     * @return
     */
    private static Merge getGroupByMerge(String database, String table,DefaultCommandSelectStatement selectStatement, List<ResultSet> resultSets){
        List<AliasField> queryField = selectStatement.getQueryField();
        if(queryField.size() != MAX_QUERY_FIELD_SIZE){
            throw new SqlParserException("the group by statement , must be query " + MAX_QUERY_FIELD_SIZE + " fields ,it contains an function and a group by filed, statement:" + selectStatement);

        }
        SimpleField groupField = selectStatement.getGroupField();

        int groupFieldIndex = -1;
        int functionIndex;

        for (int i = 0; i < queryField.size(); i++) {
            if(groupField.getName().equals(queryField.get(i).getName())){
                groupFieldIndex = i;
                break;
            }
        }

        if(groupFieldIndex == -1){
            throw new SqlParserException("group by statement not has query group by field , " + selectStatement);
        }

        functionIndex = groupFieldIndex == 0 ? 1 : 0;

        return GroupByMerge.getMerge(database, table, resultSets, queryField.get(functionIndex).getFunctionType(),functionIndex,groupFieldIndex);
    }
}
