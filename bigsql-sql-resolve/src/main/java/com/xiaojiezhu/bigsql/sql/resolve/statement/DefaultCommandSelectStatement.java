package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.sql.resolve.CrudType;
import com.xiaojiezhu.bigsql.sql.resolve.SqlResolveUtil;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SimpleField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SortField;
import com.xiaojiezhu.bigsql.sql.resolve.table.ConditionStatement;
import com.xiaojiezhu.bigsql.util.BeanUtil;

import java.util.List;
import java.util.Set;

/**
 * select statement
 * @author xiaojie.zhu
 */
public final class DefaultCommandSelectStatement extends DefaultCurdStatement implements CommandSelectStatement,ConditionStatement {
    public static final String ORDER_BY_TYPE = "orderBy.type";
    protected boolean responseNull;
    /**
     * is force read master database
     */
    private boolean readMaster;

    public DefaultCommandSelectStatement(String sql, SQLStatement sqlStatement, MySqlSchemaStatVisitor visitor) {
        super(sql, sqlStatement, visitor);
    }

    public DefaultCommandSelectStatement(String sql, SQLStatement sqlStatement, MySqlSchemaStatVisitor visitor,boolean readMaster) {
        super(sql, sqlStatement, visitor);
        this.readMaster = readMaster;
    }


    @Override
    public CrudType getCrudType() {
        return CrudType.SELECT;
    }

    @Override
    public List<AliasField> getQueryField() {
        if(sqlStatement instanceof SQLSelectStatement){
            List<AliasField> queryField = SqlResolveUtil.getQueryField((SQLSelectStatement) sqlStatement);
            return queryField;
        }else{
            throw new BigSqlException(400,"not support statement : " + sqlStatement.getClass().getName());
        }
    }

    @Override
    public SimpleField getGroupField() {
        Set<TableStat.Column> groupByColumns = visitor.getGroupByColumns();
        if(BeanUtil.isEmpty(groupByColumns)){
           return null;
        }else{

            TableStat.Column[] columns = groupByColumns.toArray(new TableStat.Column[]{});
            String name = columns[0].getName();
            return new SimpleField(name);
        }

    }

    @Override
    public SortField getOrderField() {
        List<TableStat.Column> orderByColumns = visitor.getOrderByColumns();
        if(BeanUtil.isEmpty(orderByColumns)){
            return null;
        }else{
            TableStat.Column[] columns = orderByColumns.toArray(new TableStat.Column[]{});
            TableStat.Column column = columns[0];
            String name = column.getName();

            boolean asc = true;
            Object type = column.getAttributes().get(ORDER_BY_TYPE);
            if(SqlConstant.DESC.equals(String.valueOf(type))){
                asc = false;
            }
            return new SortField(name,asc);
        }
    }

    @Override
    public boolean isReadMaster() {
        return this.readMaster;
    }

    public void setReadMaster(boolean readMaster) {
        this.readMaster = readMaster;
    }

    @Override
    public List<ConditionField> getConditionFields() {
        List<ConditionField> condition = SqlResolveUtil.getCondition(sqlStatement);
        return condition;
    }


    public boolean isResponseNull() {
        return responseNull;
    }

    public void setResponseNull(boolean responseNull) {
        this.responseNull = responseNull;
    }
}
