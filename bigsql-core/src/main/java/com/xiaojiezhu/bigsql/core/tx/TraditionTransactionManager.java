package com.xiaojiezhu.bigsql.core.tx;

import com.xiaojiezhu.bigsql.common.exception.TransactionException;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * time 2018/6/28 10:34
 * tradition transaction manager
 * @author xiaojie.zhu <br>
 */
public class TraditionTransactionManager implements TransactionManager {
    public final static Logger LOG = LoggerFactory.getLogger(TraditionTransactionManager.class);

    private DataSourcePool dataSourcePool;

    private TxConnectionPool txConnectionPool;
    /**
     * is open transaction
     */
    private boolean open = false;

    /**
     * is auto commit transaction
     */
    private boolean autoCommit = true;



    public TraditionTransactionManager(DataSourcePool dataSourcePool) {
        this.dataSourcePool = dataSourcePool;
    }



    @Override
    public void beginTransaction(boolean autoCommit) throws TransactionException {
        if(this.open){
            throw new TransactionException("begin transaction fail ,  transaction is open , you can not open again!");
        }else{
            this.autoCommit = autoCommit;
            this.open = true;
            this.txConnectionPool = new ReuseTxConnectionPool(dataSourcePool , this.autoCommit);
        }
    }

    @Override
    public void commit() throws TransactionException {
        if(!this.open){
            throw new TransactionException("commit transaction fail ,  transaction is not open");
        }else{
            this.txConnectionPool.commitTransaction();
            this.open = false;
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if(!this.open){
            throw new TransactionException("rollback transaction fail ,  transaction is not open");
        }else{
            this.txConnectionPool.rollbackTransaction();
            this.open = false;
        }
    }


    @Override
    public Connection getConnection(String datasourceName) throws SQLException {
        return txConnectionPool.getConnection(datasourceName);
    }

    @Override
    public void returnConnection(Connection connection) {
        txConnectionPool.returnConnection(connection);
    }


    @Override
    public boolean isOpenTransaction() {
        return this.open;
    }

    @Override
    public boolean isAutoCommit() {
        return this.autoCommit;
    }

}
