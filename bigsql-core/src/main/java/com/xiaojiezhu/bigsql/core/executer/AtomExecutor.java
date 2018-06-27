package com.xiaojiezhu.bigsql.core.executer;

import com.xiaojiezhu.bigsql.common.SqlConstant;
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


    public AtomExecutor(List<ExecuteResult> executeResults, Connection connection, String sql, CountDownLatch cd , Object lock) {
        this.executeResults = executeResults;
        this.connection = connection;
        this.sql = sql;
        this.cd = cd;
        this.lock = lock;
    }

    @Override
    public void run() {
        ExecuteResult executeResult = new ExecuteResult();
        try {
            Statement statement = connection.createStatement();
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
            //close();
        }
        synchronized (lock){
            executeResults.add(executeResult);
            cd.countDown();
        }

    }


}