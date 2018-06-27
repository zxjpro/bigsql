package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.executer.Executor;
import com.xiaojiezhu.bigsql.core.executer.QueryExecutor;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.ExecuteInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.core.merge.ItemMerge;
import com.xiaojiezhu.bigsql.core.merge.Merge;
import com.xiaojiezhu.bigsql.core.schema.Schema;
import com.xiaojiezhu.bigsql.core.schema.database.LogicDatabase;
import com.xiaojiezhu.bigsql.core.schema.table.LogicTable;
import com.xiaojiezhu.bigsql.core.schema.table.StrategyTable;
import com.xiaojiezhu.bigsql.sharding.ExecuteBlock;
import com.xiaojiezhu.bigsql.sharding.Strategy;
import com.xiaojiezhu.bigsql.sharding.masterslave.MasterSlaveStrategy;
import com.xiaojiezhu.bigsql.sharding.sharding.ShardingStrategy;
import com.xiaojiezhu.bigsql.sql.resolve.CrudType;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.DefaultCommandSelectStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.DefaultCurdStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;
import com.xiaojiezhu.bigsql.util.Asserts;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import com.xiaojiezhu.bigsql.util.IOUtil;

import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class CrudStatementInvoker extends StatementInvoker {
    public final static Logger LOG = LoggerFactory.getLogger(CrudStatementInvoker.class);
    protected final BigsqlContext context;
    protected final String databaseName;
    protected final EventLoopGroup loopGroup;

    public CrudStatementInvoker(Statement statement, BigsqlContext context, EventLoopGroup loopGroup, String databaseName) {
        super(statement);
        this.context = context;
        this.databaseName = databaseName;
        this.loopGroup = loopGroup;
    }

    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        if(statement instanceof DefaultCommandSelectStatement){
            DefaultCommandSelectStatement selectStatement = (DefaultCommandSelectStatement) statement;
            if(selectStatement.isResponseNull()){
                BigsqlResultSet resultSet = BigsqlResultSet.createNullResultSet(selectStatement);
                return DefaultSelectInvokeResult.createInstance(resultSet);
            }
        }

        LogicTable table = getLogicTable((DefaultCurdStatement) statement);
        if(table instanceof StrategyTable){
            StrategyTable strategyTable = (StrategyTable) table;
            InvokeResult invokeResult = null;
            try {
                invokeResult = invokeTable(strategyTable);
            } catch (Exception e) {
                throw new InvokeStatementException("invoke table fail " , e);
            }
            return invokeResult;
        }else{
            throw new BigSqlException(300, "not support table  , " + table.getDatabaseName() +"." + table.getName());
        }
    }


    /**
     * invoke sharding table
     * @param table sharding table
     * @return
     */
    private InvokeResult invokeTable(StrategyTable table) {
        Strategy strategy = table.getStrategy(statement);

        if(strategy instanceof ShardingStrategy){
            //invoke sharding
            InvokeResult invokeResult;
            Executor<?> executor = new QueryExecutor(context.getDataSourcePool(),loopGroup,context.getBigsqlConfiguration().getExecuteConcurrent());

            try {
                invokeResult = invokeShardingTable((ShardingStrategy) strategy, executor, table.getName());
            } finally {
                IOUtil.close(executor);
            }

            return invokeResult;

        }else if(strategy instanceof MasterSlaveStrategy){
            //invoke master slave
            InvokeResult invokeResult;
            Executor<?> executor = new QueryExecutor(context.getDataSourcePool(),loopGroup,context.getBigsqlConfiguration().getExecuteConcurrent());

            try {
                invokeResult = invokeMasterSlaveTable(table, (MasterSlaveStrategy) strategy, executor);
            } finally {
                IOUtil.close(executor);
            }

            return invokeResult;

        }else{
            throw new BigSqlException(300 , "not support strategy , " + strategy.getClass().getName());
        }
    }


    /**
     * invoke master slave table
     * @param table
     * @param strategy
     * @param executor
     * @return
     */
    private InvokeResult invokeMasterSlaveTable(StrategyTable table, MasterSlaveStrategy strategy, Executor<?> executor) {
        InvokeResult invokeResult;ExecuteBlock executeBlock = strategy.getExecuteBlock();
        invokeResult = executeResult(executor, table.getName(),Arrays.asList(executeBlock));
        return invokeResult;
    }

    /**
     * invoke sharding table
     * @param shardingStrategy
     * @param executor
     * @param tableName
     * @return
     */
    private InvokeResult invokeShardingTable(ShardingStrategy shardingStrategy,Executor<?> executor,String tableName){
        List<ExecuteBlock> executeBlockList = shardingStrategy.getExecuteBlockList();
        Asserts.collectionIsNotNull(executeBlockList,"execute block list can not be null");
        LOG.debug("sharding sql list : " + executeBlockList);

        return executeResult(executor, tableName, executeBlockList);
    }


    /**
     * execute result
     * @param executor
     * @param tableName
     * @param executeBlockList
     * @return
     */
    private InvokeResult executeResult(Executor<?> executor, String tableName, List<ExecuteBlock> executeBlockList) {
        if(CrudType.SELECT.equals(((CrudStatement) statement).getCrudType())){
            List<ResultSet> executeResult = getExecuteResult(executor,executeBlockList);
            Merge merge = new ItemMerge(databaseName,tableName,executeResult);
            ResultSet resultSet = merge.merge();
            return DefaultSelectInvokeResult.createInstance(resultSet);
        }else{
            //insert,update,delete
            List<Integer> executeResult = getExecuteResult(executor, executeBlockList);
            long count = BeanUtil.count(executeResult);
            ExecuteInvokeResult executeInvokeResult = new ExecuteInvokeResult(true);
            executeInvokeResult.setAffectRow((int) count);
            return executeInvokeResult;
        }
    }


    /**
     * get the sharding execute result
     * @param executeBlockList
     * @param <T>
     * @return
     * @throws InvokeStatementException
     */
    private <T> T getExecuteResult(Executor<?> executor,List<ExecuteBlock> executeBlockList) throws InvokeStatementException{
        LOG.info("execute block =============================");
        LOG.info(executeBlockList.toString());
        LOG.info("execute block =============================");
        try {
            executor.execute(executeBlockList);
        } catch (Exception e) {
            throw new InvokeStatementException("execute sql error : " + e.getMessage() , e);
        }
        List<ResultSet> result;
        try {
            result = (List<ResultSet>) executor.getResult();
        } catch (Exception e) {
            throw new InvokeStatementException("get sql execute result error : " + e.getMessage() , e);
        }

        return (T) result;
    }

    /**
     * get the sql statement query logic table
     * @param selectStatement
     * @return
     */
    private LogicTable getLogicTable(DefaultCurdStatement selectStatement) {
        List<TableName> tables = selectStatement.getTables();
        TableName primaryTable = getPrimaryTable(tables);
        Schema schema = context.getSchema();
        LogicDatabase database = schema.getDatabase(databaseName);
        return database.getTable(primaryTable.getTableName());
    }

    private TableName getPrimaryTable(List<TableName> tables){
        return tables.get(0);
    }
}
