package com.xiaojiezhu.bigsql.sharding;

import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;

/**
 * MasterSlate and sharding strategy
 * @author xiaojie.zhu
 */
public interface Strategy {

    /**
     * get strategy name
     * @return strategy name
     */
    String getName();

    /**
     * get the sql statement
     * @return statement
     */
    CrudStatement getStatement();
}
