package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.xiaojiezhu.bigsql.sql.resolve.CrudType;
import com.xiaojiezhu.bigsql.sql.resolve.SqlResolveUtil;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.table.ConditionStatement;
import com.xiaojiezhu.bigsql.sql.resolve.table.SimpleTableName;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class UpdateStatement extends DefaultCurdStatement implements ConditionStatement {
    public UpdateStatement(String sql, SQLStatement sqlStatement, MySqlSchemaStatVisitor visitor) {
        super(sql, sqlStatement, visitor);
    }

    @Override
    public CrudType getCrudType() {
        return CrudType.UPDATE;
    }

    @Override
    public List<TableName> getTables() {
        SQLName tableName = ((MySqlUpdateStatement) sqlStatement).getTableName();
        String simpleName = tableName.getSimpleName();
        return Arrays.asList(new SimpleTableName(simpleName));
    }

    @Override
    public List<ConditionField> getConditionFields() {
        List<ConditionField> condition = SqlResolveUtil.getCondition(sqlStatement);
        return condition;
    }
}
