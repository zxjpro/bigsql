package com.xiaojiezhu.bigsql.core.merge;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.MergeException;
import com.xiaojiezhu.bigsql.common.exception.SqlNotSupportException;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.ResultSetUtil;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.core.type.TypeFactory;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * time 2018/8/8 14:28
 *
 * @author xiaojie.zhu <br>
 */
public abstract class GroupByMerge implements Merge {
    public final static Logger LOG = LoggerFactory.getLogger(GroupByMerge.class);

    private String databaseName;
    private String tableName;
    private List<ResultSet> resultSets;

    private AliasField.FunctionType functionType;

    /**
     * the function in query field index
     */
    private int functionIndex;

    /**
     * the group by field in query index
     */
    private int groupFieldIndex;

    public GroupByMerge(String databaseName, String tableName, List<ResultSet> resultSets, AliasField.FunctionType functionType, int functionIndex, int groupFieldIndex) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.resultSets = resultSets;
        this.functionType = functionType;
        this.functionIndex = functionIndex;
        this.groupFieldIndex = groupFieldIndex;
    }

    @Override
    public ResultSet merge() throws MergeException {
        return merge_();
    }


    /**
     * merge max
     * @return
     */
    protected ResultSet merge_(){
        List<Field> fields = getFields();
        Map<String,Object> tmp = new HashMap<>(MergeFactory.MAX_QUERY_FIELD_SIZE);
        for (int i = 0; i < resultSets.size(); i++) {
            ResultSet resultSet = resultSets.get(i);

            try {
                while (resultSet.next()){
                    //the result set is begin from 1
                    String group = resultSet.getString(groupFieldIndex + 1);
                    Object val = resultSet.getObject(functionIndex + 1);

                    Object o = tmp.get(group);
                    if(o == null){
                        tmp.put(group,val);
                    }else{
                        Object reduceValue = this.reduce(o, val);
                        tmp.put(group,reduceValue);
                    }

                }
            } catch (SQLException e) {
                LOG.error("merge resultSet error , resultSet:" + resultSet , e);
                throw new MergeException(e.getErrorCode(),e.getMessage(),e);
            }
        }
        return createResultSet(fields, tmp);
    }

    /**
     * 合并每一次resultSet遍历时出现的值
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    protected abstract  <T> T reduce(T t1 , T t2);


    /**
     * create resultSet
     * @param fields result set field
     * @param mapData result set row data
     * @return result
     */
    private ResultSet createResultSet(List<Field> fields , Map<String,?> mapData){
        List<Type[]> rowData = new ArrayList<>(mapData.size());
        Iterator<? extends Map.Entry<String, ?>> iterator = mapData.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, ?> entry = iterator.next();
            Type<?> keyType = TypeFactory.getType(entry.getKey());
            Type<?> valueType = TypeFactory.getType(entry.getValue());

            Type<?>[] types = new Type[fields.size()];
            types[groupFieldIndex] = keyType;
            types[functionIndex] = valueType;

            rowData.add(types);
        }


        BigsqlResultSet resultSet = BigsqlResultSet.createInstance(fields, rowData);
        return resultSet;
    }


    /**
     * get the resultSet fields
     * @return
     */
    private List<Field> getFields() {
        try {
            List<Field> fields = ResultSetUtil.getFields(databaseName, tableName, resultSets.get(0));
            if(fields.size() != MergeFactory.MAX_QUERY_FIELD_SIZE){
                throw new SqlParserException("the query fields max be equals " + MergeFactory.MAX_QUERY_FIELD_SIZE);
            }
            return fields;
        } catch (SQLException e) {
            LOG.error("merge resultSet error",e);
            throw new MergeException("merge resultSet error" , e);
        }
    }

    protected static boolean isInteger(Object val){
        try {
            long v = Long.parseLong(String.valueOf(val));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static GroupByMerge getMerge(String databaseName, String tableName, List<ResultSet> resultSets, AliasField.FunctionType functionType, int functionIndex, int groupFieldIndex){
        if(AliasField.FunctionType.COUNT == functionType){
            return new GroupSumMerge(databaseName, tableName, resultSets, functionType, functionIndex, groupFieldIndex);
        }else if(AliasField.FunctionType.SUM == functionType){
            return new GroupSumMerge(databaseName, tableName, resultSets, functionType, functionIndex, groupFieldIndex);
        }else if(AliasField.FunctionType.MAX == functionType){
            return new GroupMaxMerge(databaseName, tableName, resultSets, functionType, functionIndex, groupFieldIndex);
        }else if(AliasField.FunctionType.MIN == functionType){
            return new GroupMinMerge(databaseName, tableName, resultSets, functionType, functionIndex, groupFieldIndex);
        }else{
            throw new SqlNotSupportException("not support function : " + functionType);
        }
    }
}
