package com.xiaojiezhu.bigsql.core.tx;

import com.xiaojiezhu.bigsql.common.exception.TransactionException;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    /**
     * is open transaction
     */
    private boolean open = false;

    private Set<Connection> connections;

    @Override
    public boolean isOpenTransaction() {
        return this.open;
    }

    @Override
    public void beginTransaction() throws TransactionException {
        if(this.open){
            throw new TransactionException("begin transaction fail ,  transaction is open , you can not open again!");
        }else{
            this.connections = new LinkedHashSet<>();
            this.open = true;
        }
    }

    @Override
    public void commit() throws TransactionException {
        if(!this.open){
            throw new TransactionException("commit transaction fail ,  transaction is not open");
        }else{
            if(!BeanUtil.isEmpty(connections)){
                TransactionException ex = null;
                for (Connection connection : connections) {
                    try {
                        connection.commit();
                    } catch (SQLException e) {
                        LOG.error("commit connection fail :" + connection , e);
                        ex = new TransactionException("commit connection fail " , e);
                    }
                }

                try {
                    closeConnection();
                } catch (TransactionException e) {
                    ex = e;
                }

                this.open = false;

                if(ex != null){
                    throw ex;
                }
            }else{
                this.open = false;
            }
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if(!this.open){
            throw new TransactionException("rollback transaction fail ,  transaction is not open");
        }else{
            if(!BeanUtil.isEmpty(connections)){

                TransactionException ex = null;

                for (Connection connection : connections) {
                    try {
                        connection.rollback();
                    } catch (SQLException e) {
                        LOG.error("rollback connection fail :" + connection , e);
                        ex = new TransactionException("rollback connection fail " , e);
                    }
                }

                try {
                    closeConnection();
                } catch (TransactionException e) {
                    ex = e;
                }

                this.open = false;

                if(ex != null){
                    throw ex;
                }
            }else{
                this.open = false;
            }
        }
    }

    /**
     * close the connection
     * @throws TransactionException throw the last throw exception
     */
    protected void closeConnection() throws TransactionException {
        if(!BeanUtil.isEmpty(connections)){
            SQLException exception = null;

            for (Connection connection : connections) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error("close connection fail : " + connection , e);
                    exception = e;
                }
            }

            if(exception != null){
                throw new TransactionException("close connection fail " , exception);
            }
        }
    }

}
