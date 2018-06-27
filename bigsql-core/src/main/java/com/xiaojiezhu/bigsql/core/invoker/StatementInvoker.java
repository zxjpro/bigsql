package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;

/**
 * invoke statement
 * @author xiaojie.zhu
 */
public abstract class StatementInvoker {

    protected Statement statement;

    public StatementInvoker(Statement statement) {
        this.statement = statement;
    }

    /**
     * invoker statement and return a result
     * @return result
     */
    public abstract InvokeResult invoke()throws InvokeStatementException;

    protected void throwNotSupport(){
        throw new BigSqlException("not support statement , sql : " + statement.getSql() + " , " + statement.getClass().getName());
    }

}