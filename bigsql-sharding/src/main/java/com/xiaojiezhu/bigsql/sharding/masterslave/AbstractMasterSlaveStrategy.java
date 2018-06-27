package com.xiaojiezhu.bigsql.sharding.masterslave;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.ExecuteBlock;
import com.xiaojiezhu.bigsql.sql.resolve.statement.*;

/**
 * time 2018/6/23 10:10
 *
 * @author xiaojie.zhu <br>
 */
public abstract class AbstractMasterSlaveStrategy implements MasterSlaveStrategy {
    protected CrudStatement crudStatement;
    protected DataSourcePool dataSourcePool;

    public AbstractMasterSlaveStrategy(CrudStatement crudStatement , DataSourcePool dataSourcePool) {
        this.crudStatement = crudStatement;
        this.dataSourcePool = dataSourcePool;
    }

    @Override
    public ExecuteBlock getExecuteBlock() {
        boolean readMaster = false;
        if(crudStatement instanceof CommandSelectStatement){
            if(((CommandSelectStatement) crudStatement).isReadMaster()){
                readMaster = true;
            }
        }else if(isWrite()){
            readMaster = true;
        }else{
            throw new BigSqlException(300 , "not support statement : " + crudStatement.getClass());
        }

        String dataSourceName = getDataSourceName(readMaster);
        return new ExecuteBlock(dataSourceName,crudStatement.getSql());
    }


    /**
     * get the datasource name
     * @param readMaster is read the master database
     * @return datasource name
     */
    protected abstract String getDataSourceName(boolean readMaster);


    private boolean isWrite(){
        return crudStatement instanceof InsertStatement
                || crudStatement instanceof DeleteStatement
                || crudStatement instanceof UpdateStatement;
    }

    @Override
    public String getName() {
        return getClass().getName();
    }

    @Override
    public CrudStatement getStatement() {
        return crudStatement;
    }
}
