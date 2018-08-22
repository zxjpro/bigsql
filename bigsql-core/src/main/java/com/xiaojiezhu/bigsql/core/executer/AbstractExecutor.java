package com.xiaojiezhu.bigsql.core.executer;

import com.xiaojiezhu.bigsql.common.FixRunner;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.core.context.ConnectionContext;
import com.xiaojiezhu.bigsql.core.context.CurrentStatement;
import com.xiaojiezhu.bigsql.core.tx.TransactionManager;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.ExecuteBlock;
import com.xiaojiezhu.bigsql.util.IOUtil;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author xiaojie.zhu
 */
public abstract class AbstractExecutor<T> implements Executor<T> {
    public final static Logger LOG = LoggerFactory.getLogger(AbstractExecutor.class);

    protected final ConnectionContext connectionContext;
    protected final TransactionManager transactionManager;
    protected final CurrentStatement currentStatement;
    protected final EventLoopGroup group;
    protected int concurrent;
    private boolean execute = false;
    private List<ExecuteResult> executeResults;



    /**
     *
     * @param connectionContext
     * @param group
     * @param concurrent the concurrent execute number
     */
    public AbstractExecutor(ConnectionContext connectionContext, EventLoopGroup group, int concurrent) {
        this.connectionContext = connectionContext;
        this.transactionManager = connectionContext.getTransactionManager();
        this.currentStatement = connectionContext.getCurrentStatement();
        this.group = group;
        this.concurrent = concurrent;
    }

    @Override
    public void execute(List<ExecuteBlock> executeBlocks)throws Exception {
        if(execute){
            throw new BigSqlException(300,"can not invoke again");
        }
        this.execute = true;
        this.executeResults = new ArrayList<>(executeBlocks.size());
        CountDownLatch cd = new CountDownLatch(executeBlocks.size());

        FixRunner fixRunner = new FixRunner(this.concurrent,group);
        Object lock = new Object();
        for (ExecuteBlock executeBlock : executeBlocks) {
            Connection connection = null;
            try {
                connection = transactionManager.getConnection(executeBlock.getDataSourceName());
            } catch (SQLException e) {
                throw new BigSqlException(400,"can not create connection , dataSourceName : " + executeBlock.getDataSourceName());
            }

            AtomExecutor executor = new AtomExecutor(executeResults,connection,executeBlock.getSql() , cd , lock,transactionManager,currentStatement);
            fixRunner.run(executor);
        }
        cd.await();
        //sql execute complete
        currentStatement.executeComplete();
    }


    @Override
    public T getResult() throws Exception {
        if(!execute){
            throw new BigSqlException(300,"not invoke execute() method");
        }
        for (ExecuteResult executeResult : this.executeResults) {
            if(!executeResult.isResult()){
                throw executeResult.getE();
            }
        }

        return getResult0(executeResults);
    }


    protected abstract T getResult0(List<ExecuteResult> executeResults);

}
