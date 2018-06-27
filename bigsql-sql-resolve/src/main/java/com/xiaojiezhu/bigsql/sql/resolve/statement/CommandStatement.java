package com.xiaojiezhu.bigsql.sql.resolve.statement;

/**
 * the simple command
 * <pre>
 *     set
 * </pre>
 *
 * @author xiaojie.zhu
 */
public class CommandStatement implements Statement{
    protected String sql;

    public CommandStatement(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return this.sql;
    }
}
