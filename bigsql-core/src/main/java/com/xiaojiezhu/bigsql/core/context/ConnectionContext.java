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

    /**
     * set the login username
     * @param username
     */
    void setUserName(String username);

    /**
     * get the current connection login username
     * @return
     */
    String getUserName();

    /**
     * set connection id
     * @param connectionId
     */
    void setConnectionId(int connectionId);

    /**
     * get the connection id
     * @return
     */
    int getConnectionId();

    /**
     * get connection time
     * @return
     */
    long getConnectionTime();

    /**
     * the connection host
     * @param host
     */
    void setHost(String host);

    /**
     * get host
     * @return
     */
    String getHost();
}
