package com.xiaojiezhu.bigsql.core.schema.table;

import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.sharding.Strategy;
import com.xiaojiezhu.bigsql.sharding.rule.masterslave.MasterSlaveRule;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;

/**
 * time 2018/6/23 10:34
 * the master slave table
 * @author xiaojie.zhu <br>
 */
public class DefaultMasterSlaveTable extends SimpleTable implements StrategyTable {

    public DefaultMasterSlaveTable(String databaseName, String name, BigsqlContext context, MasterSlaveRule rule) {
        super(databaseName, name, context, rule);
    }

    @Override
    public Strategy getStrategy(Statement statement) {
        Strategy strategy = createStrategy(statement);
        return strategy;
    }

}
