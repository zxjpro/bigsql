package com.xiaojiezhu.bigsql.core.tx;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.TransactionException;
import com.xiaojiezhu.bigsql.core.configuration.datasource.BigsqlConnection;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * time 2018/8/9 14:58
 *
 * @author xiaojie.zhu <br>
 */
class ReuseTxConnectionPool implements TxConnectionPool {
    public final static Logger LOG = LoggerFactory.getLogger(ReuseTxConnectionPool.class);

    /**
     * save the borrow connection from this pool,
     *
     * if return connection ,it will be remove
     */
    private final List<Integer> borrowHashCode = new LinkedList<>();

    private final Object lock = new Object();

    private DataSourcePool dataSourcePool;

    /**
     * client reuse connection , <dataSourceName , ConnectionList>
     */
    private final Map<String, Queue<Connection>> reusePool = new HashMap<>(1);

    /**
     * is auto commit transaction
     */
    private boolean autoCommit;


    /**
     * is complete commit or rollback
     */
    private boolean complete;

    /**
     * connection is close
     */
    private boolean close;

    public ReuseTxConnectionPool(DataSourcePool dataSourcePool,boolean autoCommit) {
        this.dataSourcePool = dataSourcePool;
        this.autoCommit = autoCommit;
    }

    /**
     * this is a thread safe method
     * @param dataSourceName dataSourceName
     * @return
     * @throws SQLException
     */
    @Override
    public Connection getConnection(String dataSourceName) throws SQLException {
        if(complete){
            throw new BigSqlException(300 , "tx pool is commit or rollback , you can not get connection");
        }

        synchronized (lock){

            Queue<Connection> connections = reusePool.get(dataSourceName);
            if(connections == null){
                connections = new LinkedList<>();
                reusePool.put(dataSourceName,connections);
            }
            Connection connection = connections.poll();
            if(connection != null){
                this.borrowHashCode.add(connection.hashCode());
                return connection;
            }else{
                connection = dataSourcePool.getDataSource(dataSourceName).getConnection();
                if(!this.autoCommit){
                    connection.setAutoCommit(false);
                }
                this.borrowHashCode.add(connection.hashCode());
                return connection;

            }

        }
    }

    @Override
    public void returnConnection(Connection conn) {
        if(complete){
            throw new BigSqlException(300 , "tx pool is commit or rollback , you can not return connection");
        }

        Asserts.notNull(conn,"return connection can not be null");

        BigsqlConnection connection = (BigsqlConnection) conn;

        synchronized (lock){
            int hashCode = connection.hashCode();
            if(this.borrowHashCode.contains(hashCode)){
                // if return ,the borrow hash code will be remove
                int index = this.borrowHashCode.indexOf(hashCode);
                this.borrowHashCode.remove(index);

                Queue<Connection> connections = this.reusePool.get(connection.getDataSourceName());
                connections.add(conn);
            }else{
                throw new BigSqlException(300,"the connection is not create from this tx pool :" + connection);
            }
        }


    }

    @Override
    public void commitTransaction() throws TransactionException {
        synchronized (lock){
            if(this.autoCommit){
                this.complete = true;

                this.close0();
            }else{
                try {
                    this.commitTransactionReal();
                } finally {
                    this.close0();

                }
            }
        }

    }



    private void close0() {
        try {
            this.close();
        } catch (IOException e) {
            throw new TransactionException("close connection fail" , e);
        }
    }

    /**
     * real commit
     */
    private void commitTransactionReal() {
        this.checkOver();
        this.complete = true;

        TransactionException ex = null;
        Iterator<Map.Entry<String, Queue<Connection>>> iterator = reusePool.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, Queue<Connection>> entry = iterator.next();
            Queue<Connection> connections = entry.getValue();

            for (Connection connection : connections) {
                try {
                    connection.commit();
                } catch (SQLException e) {
                    ex = new TransactionException("commit connection fail " , e);
                }
            }
        }
        //for each over

        if(ex != null){
            throw ex;
        }
    }

    @Override
    public void rollbackTransaction() throws TransactionException {
        if(this.autoCommit){
            throw new BigSqlException(300 , "this transaction is auto commit , can not rollback");
        }

        synchronized (lock){
            try {
                this.rollbackTransactionReal();
            } finally {
                this.close0();
            }
        }
    }

    /**
     * real rollback
     */
    private void rollbackTransactionReal() {
        this.checkOver();
        this.complete = true;

        TransactionException ex = null;
        Iterator<Map.Entry<String, Queue<Connection>>> iterator = reusePool.entrySet().iterator();

        while (iterator.hasNext()){
            Map.Entry<String, Queue<Connection>> entry = iterator.next();
            Queue<Connection> connections = entry.getValue();

            for (Connection connection : connections) {
                try {
                    connection.rollback();
                } catch (SQLException e) {
                    ex = new TransactionException("rollback connection fail " , e);
                }
            }

        }
        //for each over

        if(ex != null){
            throw ex;
        }
    }

    private void checkOver() {
        if(close){
            throw new BigSqlException(300 , "tx poll is close");
        }
        if(complete){
            throw new BigSqlException(300 , "tx pool is commit or rollback");
        }
        if(!this.isReturnAll()){
            throw new BigSqlException(300 , "tx poll borrow connection is not return all");
        }
    }


    /**
     * close的条件是需要归还所有借出去的连接
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        synchronized (lock){
            if(this.close){
                throw new BigSqlException(300 , "tx poll is close ,you can close again!");
            }

            if(!this.isReturnAll()){
                throw new BigSqlException(300 , "tx poll borrow connection is not return all");
            }

            if(!this.complete){
                throw new BigSqlException(300 , "tx pool not commit or rollback , can not close");
            }

            this.close = true;


            SQLException exception = null;
            Connection connection;
            Iterator<Map.Entry<String, Queue<Connection>>> iterator = reusePool.entrySet().iterator();

            while (iterator.hasNext()){
                Map.Entry<String, Queue<Connection>> entry = iterator.next();
                Queue<Connection> connections = entry.getValue();


                while ((connection = connections.poll()) != null){

                    try {
                        connection.close();
                    } catch (SQLException e) {
                        LOG.error("close connection fail : " + connection , e);
                        exception = e;
                    }

                }
            }
            //for each over

            if(exception != null){
                throw new IOException("close connection fail " , exception);
            }
        }
    }

    /**
     * is return all connection
     * @return
     */
    protected boolean isReturnAll(){
        return borrowHashCode.size() == 0;
    }
}
