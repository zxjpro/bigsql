package com.xiaojiezhu.bigsql.core.executer;

import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.core.configuration.datasource.BigsqlConnection;
import com.xiaojiezhu.bigsql.core.context.CurrentStatement;
import com.xiaojiezhu.bigsql.core.context.ShardingBlock;
import com.xiaojiezhu.bigsql.core.tx.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * run the atom connection
 * @author xiaojie.zhu
 */
public class AtomExecutor implements Runnable{
    public final static Logger LOG = LoggerFactory.getLogger(AtomExecutor.class);
    private final List<ExecuteResult> executeResults;
    private Connection connection;
    private String sql;
    private final CountDownLatch cd;
    private final Object lock;

    private final TransactionManager transactionManager;
    private final CurrentStatement currentStatement;


    public AtomExecutor(List<ExecuteResult> executeResults, Connection connection, String sql, CountDownLatch cd , Object lock, TransactionManager transactionManager, CurrentStatement currentStatement) {
        this.executeResults = executeResults;
        this.connection = connection;
        this.sql = sql;
        this.cd = cd;
        this.lock = lock;
        this.transactionManager = transactionManager;
        this.currentStatement = currentStatement;
    }

    @Override
    public void run() {
        ShardingBlock block = new ShardingBlock();
        block.setSql(this.sql);
        block.start();
        String dataSourceName = ((BigsqlConnection) connection).getDataSourceName();
        block.setDataSourceName(dataSourceName);
        ExecuteResult executeResult = new ExecuteResult();
        try {
            Statement statement = connection.createStatement();
            //保存创建statement成功的时间
            block.completeCreateStatement();
            Object result;
            if(sql.startsWith(SqlConstant.SELECT)){
                result = statement.executeQuery(sql);
            }else{
                result = statement.executeUpdate(sql);
            }
            executeResult.setResult(true);
            executeResult.setData(result);
        } catch (Exception e) {
            LOG.error("执行sql失败, connection:" + connection + " , sql:" + sql + " , " + e.getMessage() , e);
            executeResult.setResult(false);
            executeResult.setE(e);
        } finally {
            block.completeQuery();
            transactionManager.returnConnection(connection);
        }

        block.completeReturn();
        this.currentStatement.addBlock(block);

        synchronized (lock){
            executeResults.add(executeResult);
            LOG.debug("atom execute success : " + Thread.currentThread().getName());
            cd.countDown();
            block.end();
        }

    }

    @Override
    public String toString() {
        return this.sql;
    }
}
