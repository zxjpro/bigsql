package com.xiaojiezhu.bigsql.core.merge;

import com.xiaojiezhu.bigsql.common.exception.MergeException;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.ResultSetUtil;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SortField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CommandSelectStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

/**
 * time 2018/8/8 16:51
 *
 * @author xiaojie.zhu <br>
 */
public class OrderByMerge implements Merge {

    private String databaseName;
    private String tableName;
    private List<ResultSet> resultSets;
    private CommandSelectStatement selectStatement;

    private boolean asc;


    /**
     * the order by field on query index
     */
    private int orderFieldIndex = -1;


    public OrderByMerge(String databaseName, String tableName, List<ResultSet> resultSets, CommandSelectStatement selectStatement) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.resultSets = resultSets;
        this.selectStatement = selectStatement;

        this.init();
    }

    private void init() {
        SortField orderField = selectStatement.getOrderField();
        this.asc = orderField.isAsc();

        List<AliasField> queryField = selectStatement.getQueryField();
        for (int i = 0; i < queryField.size(); i++) {
            if(orderField.getName().equals(queryField.get(i).getName())){
                this.orderFieldIndex = i;
                break;
            }
        }
        if(this.orderFieldIndex == -1){
            throw new SqlParserException("the order sql must has order field in query");
        }
    }

    @Override
    public ResultSet merge() throws MergeException {
        try {
            List<Field> fields = ResultSetUtil.getFields(databaseName, tableName, resultSets.get(0));
            List<Type[]> rowData = ResultSetUtil.getRowData(resultSets);
            rowData.sort(new DefaultOrder(orderFieldIndex , asc));
            BigsqlResultSet resultSet = BigsqlResultSet.createInstance(fields, rowData);
            return resultSet;
        } catch (SQLException e) {
            throw new MergeException("merge resultSet error : " + e.getMessage() , e);
        }
    }


    /**
     * the default order
     */
    private static class DefaultOrder implements Comparator<Type[]>{
        private int orderIndex;
        private boolean asc;


        public DefaultOrder(int orderIndex, boolean asc) {
            this.orderIndex = orderIndex;
            this.asc = asc;
        }

        @Override
        public int compare(Type[] o1, Type[] o2) {
            Type t1 = o1[orderIndex];
            Type t2 = o2[orderIndex];

            if(t1 == null || t2 == null){
                if(t1 == null){
                    return -1;
                }else if(t2 == null){
                    return 1;
                }
            }

            if(asc){
                return t1.compareTo(t2);
            }else{
                return - (t1.compareTo(t2));
            }
        }
    }
}
