package com.xiaojiezhu.bigsql.sharding.masterslave;

import com.xiaojiezhu.bigsql.common.annotation.EnableStrategy;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.rule.masterslave.MasterSlaveDatasource;
import com.xiaojiezhu.bigsql.sharding.rule.masterslave.MasterSlaveRule;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;

/**
 * time 2018/6/23 15:12
 * read slave write master strategy
 * @author xiaojie.zhu <br>
 */
@EnableStrategy
public class SimpleMasterSlaveStrategy extends AbstractMasterSlaveStrategy {
    protected MasterSlaveRule masterSlaveRule;

    public SimpleMasterSlaveStrategy(CrudStatement crudStatement, DataSourcePool dataSourcePool, MasterSlaveRule masterSlaveRule) {
        super(crudStatement, dataSourcePool);
        this.masterSlaveRule = masterSlaveRule;
    }

    @Override
    protected String getDataSourceName(boolean readMaster) {
        MasterSlaveDatasource masterSlaveDatasource = masterSlaveRule.getMasterSlaveDatasource();
        if(readMaster){
            return masterSlaveDatasource.getMasterDatasourceName().get(0);
        }else{
            return masterSlaveDatasource.getSlaveDatasourceName().get(0);
        }
    }
}
