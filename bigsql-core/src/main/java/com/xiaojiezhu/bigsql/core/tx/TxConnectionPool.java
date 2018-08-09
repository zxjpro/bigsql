package com.xiaojiezhu.bigsql.core.tx;

import com.xiaojiezhu.bigsql.common.exception.TransactionException;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * time 2018/8/9 14:48
 *
 * this is a bigsql client's connection pool , it provider a reuse connection on a transaction or a cluster query and update。
 *
 * 这是一个可以重复使用的客户端连接池，可以在一次查询或者修改中，多次复用同一个连接，或者在一个数据库事务中，多次复用一个连接。
 *
 * @author xiaojie.zhu <br>
 */
interface TxConnectionPool extends Closeable {

    /**
     * get a connection , if the pool has connection ,it will return from poll 。 else create a new connection。
     *
     * if you get a connection from this ,you must returnConnection()
     * @param dataSourceName dataSourceName
     * @return
     */
    Connection getConnection(String dataSourceName)throws SQLException;

    /**
     * if you get a connection from this poll， you must call this method to return connection。
     * and you return connection must be get from this object,else throw exception
     *
     * @param connection connection
     * @return
     */
    void returnConnection(Connection connection);


    /**
     * commit transaction
     * @throws TransactionException
     */
    void commitTransaction() throws TransactionException;


    /**
     * rollback transaction
     * @throws TransactionException
     */
    void rollbackTransaction() throws TransactionException;



}
