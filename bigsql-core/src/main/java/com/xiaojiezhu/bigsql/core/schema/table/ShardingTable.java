package com.xiaojiezhu.bigsql.core.schema.table;

import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public interface ShardingTable extends StrategyTable{

    /**
     * sharding column name
     * @return
     */
    Set<String> listShardingColumn();
}
