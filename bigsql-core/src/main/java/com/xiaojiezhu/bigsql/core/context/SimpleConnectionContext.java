package com.xiaojiezhu.bigsql.core.context;

import com.xiaojiezhu.bigsql.core.tx.TraditionTransactionManager;
import com.xiaojiezhu.bigsql.core.tx.TransactionManager;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;

/**
 * time 2018/6/28 10:02
 * client connection context , save connection operation
 * @author xiaojie.zhu <br>
 */
public class SimpleConnectionContext implements ConnectionContext{
    /**
     * current use database
     */
    private String currentDatabase;
    private TransactionManager transactionManager;
    private DataSourcePool dataSourcePool;

    /**
     * this client current execute statement
     */
    private CurrentStatement currentStatement;

    /**
     * current login username
     */
    private String userName;

    private final long connectionTime;

    /**
     * the connection id
     */
    private int connectionId = -1;

    /**
     * the connection host
     */
    private String host;


    public SimpleConnectionContext(DataSourcePool dataSourcePool) {
        this.connectionTime = System.currentTimeMillis();
        this.dataSourcePool = dataSourcePool;
        this.transactionManager = new TraditionTransactionManager(dataSourcePool);
    }

    @Override
    public String getCurrentDataBase() {
        return currentDatabase;
    }

    @Override
    public void setCurrentDataBase(String dataBase) {
        this.currentDatabase = dataBase;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public void setCurrentStatement(CurrentStatement currentStatement) {
        this.currentStatement = currentStatement;
    }

    @Override
    public CurrentStatement getCurrentStatement() {
        return this.currentStatement;
    }

    @Override
    public void destroy() {
        transactionManager.rollback();
    }

    @Override
    public void setUserName(String username) {
        this.userName = username;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public int getConnectionId() {
        return this.connectionId;
    }

    @Override
    public long getConnectionTime() {
        return this.connectionTime;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return this.host;
    }


}
