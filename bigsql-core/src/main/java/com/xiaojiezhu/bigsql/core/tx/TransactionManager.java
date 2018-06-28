package com.xiaojiezhu.bigsql.core.tx;

import com.xiaojiezhu.bigsql.common.exception.TransactionException;

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
     * begin the transaction
     * @throws TransactionException
     */
    void beginTransaction()throws TransactionException;

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





}
