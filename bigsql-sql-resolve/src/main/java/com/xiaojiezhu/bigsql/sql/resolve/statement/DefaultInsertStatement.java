package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.NotSupportException;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.sql.resolve.CrudType;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.table.SimpleTableName;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.util.*;

/**
 * insert sql
 * @author xiaojie.zhu
 */
public class DefaultInsertStatement extends InsertStatement {
    private String tableName;
    private List<List<ValueField>> valueFieldsList;
    private SQLInsertStatement sqlStatement;

    public DefaultInsertStatement(String sql, SQLStatement sqlStatement, MySqlSchemaStatVisitor visitor) {
        super(sql, sqlStatement, visitor);
        this.sqlStatement = (SQLInsertStatement) sqlStatement;
    }



    @Override
    public CrudType getCrudType() {
        return CrudType.INSERT;
    }

    /**
     * 第一个List是存入了每一行插入的值，因为insert可以带有行值
     * 第二个List是存入了每一行中的每一个值
     * @return
     */
    @Override
    public List<List<ValueField>> getInsertValues(){
        if(this.valueFieldsList == null){
            this.valueFieldsList = new ArrayList<>();
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            sqlStatement.accept(visitor);
            Collection<TableStat.Column> columns = visitor.getColumns();

            List<SQLInsertStatement.ValuesClause> valuesList = sqlStatement.getValuesList();
            if(valuesList.size() > 1){
                // multi insert , not support now
                throw new NotSupportException("not support multipart inset : " + sql);
            }
            for(int i = 0 ; i < valuesList.size() ; i ++){
                List<SQLExpr> values = valuesList.get(i).getValues();
                if(columns.size() != values.size()){
                    throw new SqlParserException("insert column size not equals value size , " + sql);
                }
                List<ValueField> valueFields = new ArrayList<>(columns.size());
                Iterator<TableStat.Column> iterator = columns.iterator();

                int j = 0;
                while (iterator.hasNext()){
                    TableStat.Column column = iterator.next();
                    SQLExpr sqlExpr = values.get(j);
                    valueFields.add(new ValueField(column.getName(),sqlExpr.toString()));
                    j++;
                }
                this.valueFieldsList.add(valueFields);
            }
        }
        return valueFieldsList;
    }

    @Override
    public void addInsertColumn(String columnName, List<?> values) {
        List<SQLExpr> columns = sqlStatement.getColumns();
        columns.add(new SQLIdentifierExpr(columnName));
        List<SQLInsertStatement.ValuesClause> valuesList = sqlStatement.getValuesList();
        if(valuesList.size() != values.size()){
            throw new SqlParserException("insert sql add column ,the lines not equals add value , " + getSql() + " , addColumn:" + columnName + " addValues : " + values);
        }
        for (int i = 0 ; i < valuesList.size() ; i ++) {
            SQLInsertStatement.ValuesClause valuesClause = valuesList.get(i);
            Object value = values.get(i);
            if(value == null){
                valuesClause.addValue(new SQLNullExpr());
            }else if(TypeUtil.isNumber(value)){
                if(TypeUtil.isInt(value)){
                    valuesClause.addValue(new SQLIntegerExpr(Integer.parseInt(String.valueOf(value))));
                }else{
                    valuesClause.addValue(new SQLIntegerExpr(Long.parseLong(String.valueOf(value))));
                }

            }else{
                valuesClause.addValue(new SQLCharExpr(String.valueOf(value)));
            }
        }
        this.sql = sqlStatement.toString();
    }

    @Override
    public List<String> getInsertSql() {
        int index = sql.indexOf(SqlConstant.VALUES);
        String prefix = sql.substring(0, index);

        List<SQLInsertStatement.ValuesClause> valuesList = sqlStatement.getValuesList();
        List<String> insertSqls = new ArrayList<>(valuesList.size());
        for(int i = 0 ; i < valuesList.size() ; i++){
            SQLInsertStatement.ValuesClause valuesClause = valuesList.get(i);
            insertSqls.add(prefix + " " + valuesClause);
        }
        return insertSqls;
    }


}
