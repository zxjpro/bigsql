package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.common.exception.TransactionException;
import com.xiaojiezhu.bigsql.core.context.ConnectionContext;
import com.xiaojiezhu.bigsql.core.invoker.result.ExecuteInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.core.tx.TransactionManager;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;

/**
 * time 2018/6/28 15:53
 *
 * @author xiaojie.zhu <br>
 */
public class TransactionInvoker extends StatementInvoker {
    protected ConnectionContext connectionContext;

    public TransactionInvoker(Statement statement, ConnectionContext connectionContext) {
        super(statement);
        this.connectionContext = connectionContext;
    }

    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        TransactionManager transactionManager = connectionContext.getTransactionManager();
        if(SqlConstant.OPEN_TRANSACTION.equalsIgnoreCase(statement.getSql())){
            // begin transaction
            transactionManager.beginTransaction();
        }else if(SqlConstant.COMMIT_TRANSACTION.equalsIgnoreCase(statement.getSql())){
            //commit transaction
            transactionManager.commit();
        }else if(SqlConstant.ROLLBACK_TRANSACTION.equalsIgnoreCase(statement.getSql())){
            //rollback transaction
            transactionManager.rollback();
        }else{
            throw new TransactionException("not support transaction cmd : " + statement);
        }

        return new ExecuteInvokeResult(true);
    }
}
