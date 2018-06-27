package com.xiaojiezhu.bigsql.sql.resolve.statement;

/**
 * @author xiaojie.zhu
 */
public class ShowEngineStatement implements Statement {
    protected String sql;

    public ShowEngineStatement(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }
}
