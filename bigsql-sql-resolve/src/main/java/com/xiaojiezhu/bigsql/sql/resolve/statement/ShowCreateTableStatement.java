package com.xiaojiezhu.bigsql.sql.resolve.statement;

/**
 * SHOW CREATE TABLE 'tableName'
 * @author xiaojie.zhu
 */
public final class ShowCreateTableStatement implements Statement {
    protected String sql;
    protected String tableName;

    public ShowCreateTableStatement(String sql) {
        sql = sql.replaceAll(";","");
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }

    public String getTableName(){
        if(tableName == null){
            int index = sql.lastIndexOf(" ");
            this.tableName = sql.substring(index);
        }
        return this.tableName;
    }
}
