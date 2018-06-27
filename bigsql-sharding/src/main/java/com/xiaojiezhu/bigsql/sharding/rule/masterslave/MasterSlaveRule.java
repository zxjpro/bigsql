package com.xiaojiezhu.bigsql.sharding.rule.masterslave;

import com.xiaojiezhu.bigsql.sharding.rule.Rule;

/**
 * Master slave rule
 * @author xiaojie.zhu
 */
public interface MasterSlaveRule extends Rule {
    String MASTER_SLAVE_TAG_NAME = "masterSlave";


    MasterSlaveDatasource getMasterSlaveDatasource();

}
