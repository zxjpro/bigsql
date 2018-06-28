package com.xiaojiezhu.bigsql.core.context;

import com.xiaojiezhu.bigsql.core.tx.TraditionTransactionManager;
import com.xiaojiezhu.bigsql.core.tx.TransactionManager;

/**
 * time 2018/6/28 10:02
 * client connection context , save connection operation
 * @author xiaojie.zhu <br>
 */
public class SimpleConnectionContext implements ConnectionContext{
    private String currentDatabase;
    private TransactionManager transactionManager;

    public SimpleConnectionContext() {
        this.transactionManager = new TraditionTransactionManager();
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


    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
