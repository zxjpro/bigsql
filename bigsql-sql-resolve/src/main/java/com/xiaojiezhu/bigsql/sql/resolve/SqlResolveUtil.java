package com.xiaojiezhu.bigsql.sql.resolve;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.field.Expression;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.table.SimpleTableName;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;

import java.util.*;

/**
 * @author xiaojie.zhu
 */
public class SqlResolveUtil {

    public static List<TableName> getTableName(MySqlSchemaStatVisitor visitor){
        Map<TableStat.Name, TableStat> tables = visitor.getTables();
        Set<TableStat.Name> names = tables.keySet();
        List<TableName> tableNames = new ArrayList<>(names.size());
        for (TableStat.Name name : names) {
            tableNames.add(new SimpleTableName(name.getName(),name.getName()));
        }
        return tableNames;
    }

    public static List<AliasField> getQueryField(SQLSelectStatement selectStatement){
        SQLSelect select = selectStatement.getSelect();
        List<SQLSelectItem> selectList = select.getQueryBlock().getSelectList();
        List<AliasField> list = new ArrayList<>();
        for (SQLSelectItem selectItem : selectList) {
            String name = selectItem.getExpr().toString();
            String alias = selectItem.getAlias();
            if(alias == null){
                alias = name;
            }
            list.add(new AliasField(name,alias));
        }
        return list;
    }

    public static List<ConditionField> getCondition(SQLStatement sqlStatement){
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        List<TableStat.Condition> conditions = visitor.getConditions();
        List<ConditionField> valueFields = new ArrayList<>(conditions.size());
        for (TableStat.Condition condition : conditions) {

            ConditionField conditionField = findConditionField(valueFields, condition.getColumn().getName());
            if(conditionField == null){
                conditionField = new ConditionField(condition.getColumn().getName());
                valueFields.add(conditionField);
            }

            if(condition.getOperator().equals(SqlConstant.BETWEEN)){
                //BETWEEN AND
                System.out.println(1);
                List<Object> values = condition.getValues();
                conditionField.addExpression(new Expression(SqlConstant.GREAT_EQUALS , values.get(0)));
                conditionField.addExpression(new Expression(SqlConstant.LESS_EQUALS , values.get(1)));

            }else{
                Expression expression = new Expression(condition.getOperator(), condition.getValues().get(0));
                conditionField.addExpression(expression);
            }



        }
        return valueFields;
    }

    private static ConditionField findConditionField(List<ConditionField> conditionFields , String columnName){
        if(conditionFields != null){
            for (ConditionField conditionField : conditionFields) {
                if(conditionField.getName().equals(columnName)){
                    return conditionField;
                }
            }
        }

        return null;
    }


    /**
     * is select *
     * @param queryFields query field
     * @return
     */
    public static boolean isQueryAll(List<AliasField> queryFields){
        if(queryFields == null || queryFields.size() == 0){
            throw new NullPointerException("alias field is null size");
        }
        if(queryFields.size() == 1){
            AliasField aliasField = queryFields.get(0);
            if("*".equals(aliasField.getName()) && "*".equals(aliasField.getAsName())){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }
}
