package com.xiaojiezhu.bigsql.core.schema.table;

import com.xiaojiezhu.bigsql.sharding.Strategy;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;

/**
 * @author xiaojie.zhu
 */
public interface StrategyTable extends LogicTable{

    /**
     * get table strategy
     * @return table strategy
     */
    Strategy getStrategy(Statement statement);
}
