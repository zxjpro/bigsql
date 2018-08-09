package com.xiaojiezhu.bigsql.core.tx;

import com.xiaojiezhu.bigsql.common.exception.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * time 2018/6/28 10:04
 *
 * bigsql transaction manager
 *
 * @author xiaojie.zhu <br>
 */
public interface TransactionManager {

    /**
     * is open transaction
     * @return true of false
     */
    boolean isOpenTransaction();

    /**
     * 是否自动提交事务
     * @return
     */
    boolean isAutoCommit();

    /**
     * begin the transaction
     * @param autoCommit  is auto commit transaction , true is auto commit
     * @throws TransactionException
     */
    void beginTransaction(boolean autoCommit)throws TransactionException;

    /**
     * commit transaction
     * @throws TransactionException
     */
    void commit()throws TransactionException;

    /**
     * rollback the transaction
     * @throws TransactionException
     */
    void rollback()throws TransactionException;

    /**
     * get a connection by dataSource name , it maybe a reuse connection。
     *
     * when you use completed the connection , you must return connection to this transaction
     * @param datasourceName data sourceName
     * @return
     * @exception SQLException can not get connection
     */
    Connection getConnection(String datasourceName)throws SQLException;

    /**
     * if you get a connection from transaction , you must return the connection
     * @param connection
     */
    void returnConnection(Connection connection);





}
