package com.xiaojiezhu.bigsql.core.executer;

import com.xiaojiezhu.bigsql.core.tx.TransactionManager;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import io.netty.channel.EventLoopGroup;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class QueryExecutor extends AbstractExecutor<List<ResultSet>> {

    /**
     * @param transactionManager
     * @param group
     * @param concurrent     the concurrent execute number
     */
    public QueryExecutor(TransactionManager transactionManager, EventLoopGroup group, int concurrent) {
        super(transactionManager, group, concurrent);
    }

    @Override
    protected List<ResultSet> getResult0(List<ExecuteResult> executeResults) {
        List<ResultSet> resultSets = new ArrayList<>(executeResults.size());
        for (ExecuteResult executeResult : executeResults) {
            resultSets.add(executeResult.getData());
        }
        return resultSets;
    }
}
