package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.xiaojiezhu.bigsql.sql.resolve.SqlResolveUtil;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
public abstract class DefaultCurdStatement implements CrudStatement {
    protected String sql;

    protected SQLStatement sqlStatement;
    protected MySqlSchemaStatVisitor visitor;

    public DefaultCurdStatement(String sql,SQLStatement sqlStatement,MySqlSchemaStatVisitor visitor) {
        this.sql = sql;
        this.sqlStatement = sqlStatement;
        this.visitor = visitor;
        this.sqlStatement.accept(this.visitor);
    }

    @Override
    public List<TableName> getTables() {
        List<TableName> tableName = SqlResolveUtil.getTableName(visitor);
        return tableName;
    }

    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}
