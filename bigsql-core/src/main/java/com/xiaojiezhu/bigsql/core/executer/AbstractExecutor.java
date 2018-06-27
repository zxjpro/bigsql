package com.xiaojiezhu.bigsql.core.executer;

import com.xiaojiezhu.bigsql.common.FixRunner;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
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
    protected final DataSourcePool dataSourcePool;
    protected final EventLoopGroup group;
    protected int concurrent;
    private boolean execute = false;
    private List<ExecuteResult> executeResults;
    private List<Connection> connections;



    /**
     *
     * @param dataSourcePool
     * @param group
     * @param concurrent the concurrent execute number
     */
    public AbstractExecutor(DataSourcePool dataSourcePool, EventLoopGroup group,int concurrent) {

        this.dataSourcePool = dataSourcePool;
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
        this.connections = new ArrayList<>(executeBlocks.size());

        FixRunner fixRunner = new FixRunner(this.concurrent,group);
        Object lock = new Object();
        for (ExecuteBlock executeBlock : executeBlocks) {
            Connection connection = null;
            try {
                connection = dataSourcePool.getDataSource(executeBlock.getDataSourceName()).getConnection();
                this.connections.add(connection);
            } catch (SQLException e) {
                throw new BigSqlException(400,"can not create connection , dataSourceName : " + executeBlock.getDataSourceName());
            }

            AtomExecutor executor = new AtomExecutor(executeResults,connection,executeBlock.getSql() , cd , lock);
            fixRunner.run(executor);
        }
        cd.await();
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


    @Override
    public void close() throws IOException {
        if(!execute){
            throw new BigSqlException(300,"not invoke execute() method");
        }
        if(this.connections != null && this.connections.size() > 0){
            for (Connection connection : this.connections) {
                IOUtil.close(connection);
            }
        }
    }




    protected abstract T getResult0(List<ExecuteResult> executeResults);

}
