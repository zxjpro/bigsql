package com.xiaojiezhu.bigsql.sql.resolve.statement;

/**
 * @author xiaojie.zhu
 */
public class SettingStatement implements Statement {
    protected String sql;

    public SettingStatement(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }
}
