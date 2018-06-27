package com.xiaojiezhu.bigsql.sharding.sharding;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.sharding.ExecuteBlock;
import com.xiaojiezhu.bigsql.sharding.Strategy;

import java.util.List;

/**
 * sharding strategy
 * @author xiaojie.zhu
 */
public interface ShardingStrategy extends Strategy {

    /**
     * get the execute block list
     * @return execute block list
     */
    List<ExecuteBlock> getExecuteBlockList()throws BigSqlException;
}
