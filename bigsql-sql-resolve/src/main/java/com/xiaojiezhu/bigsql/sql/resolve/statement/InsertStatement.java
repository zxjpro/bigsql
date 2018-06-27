package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.xiaojiezhu.bigsql.sql.resolve.CrudType;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.table.SimpleTableName;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;

import java.util.Arrays;
import java.util.List;

/**
 * insert sql
 * @author xiaojie.zhu
 */
public abstract class InsertStatement extends DefaultCurdStatement {
    private String tableName;

    public InsertStatement(String sql, SQLStatement sqlStatement, MySqlSchemaStatVisitor visitor) {
        super(sql, sqlStatement, visitor);
    }


    @Override
    public List<TableName> getTables() {
        String tableName = getTableName();
        return Arrays.asList(new SimpleTableName(tableName));
    }

    public String getTableName(){
        if(this.tableName == null){
            SQLName tableName = ((SQLInsertStatement) sqlStatement).getTableName();
            this.tableName = tableName.getSimpleName();
        }
        return this.tableName;
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
    public abstract List<List<ValueField>> getInsertValues();

    /**
     * 增加一个插入的列
     * @param columnName 列名
     * @param values 插入的值
     */
    public abstract void addInsertColumn(String columnName,List<?> values);

    /**
     * 获取拆分后的INSERT语句，就是把一个INSERT插入多行，拆成多条SQL插入
     * @return INSERT SQL 列表
     */
    public abstract List<String> getInsertSql();


    @Override
    public String toString() {
        return "INSERT : " + sql;
    }
}
