package com.xiaojiezhu.bigsql.sql.resolve.statement;

/**
 * not handler,response null line
 * @author xiaojie.zhu
 */
public final class NoHandlerStatement implements Statement {
    protected String sql;

    public NoHandlerStatement(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }
}
