package com.xiaojiezhu.bigsql.core.context;

import com.xiaojiezhu.bigsql.core.tx.TransactionManager;

/**
 * time 2018/6/28 9:57
 *
 * the connection context
 * @author xiaojie.zhu <br>
 */
public interface ConnectionContext {

    /**
     * get the current select schema
     * @return if not select,return null
     */
    String getCurrentDataBase();

    /**
     * when select schema , set a flag
     * @param dataBase
     */
    void setCurrentDataBase(String dataBase);


    /**
     * create the transaction
     * @return transaction
     */
    TransactionManager getTransactionManager();

    /**
     * set the current client execute statement
     * @param currentStatement
     */
    void setCurrentStatement(CurrentStatement currentStatement);

    /**
     * get the current client execute statement
     * @return
     */
    CurrentStatement getCurrentStatement();


    /**
     * destroy
     */
    void destroy();
}
