package com.xiaojiezhu.bigsql.sharding.masterslave;

import com.xiaojiezhu.bigsql.sharding.ExecuteBlock;
import com.xiaojiezhu.bigsql.sharding.Strategy;

/**
 * MasterSlave mode
 * @author xiaojie.zhu
 */
public interface MasterSlaveStrategy extends Strategy {

    ExecuteBlock getExecuteBlock();
}
