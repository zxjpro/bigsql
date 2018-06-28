package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.context.ConnectionContext;
import com.xiaojiezhu.bigsql.core.invoker.result.ExecuteInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import io.netty.channel.Channel;

/**
 * invoke 'select schema'
 * @author xiaojie.zhu
 */
public class InitDataBaseStatementInvoker extends StatementInvoker {
    private BigsqlContext context;
    private Channel channel;


    public InitDataBaseStatementInvoker(Statement statement, BigsqlContext context, Channel channel) {
        super(statement);
        this.context = context;
        this.channel = channel;
    }

    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        ConnectionContext connectionContext = context.getConnectionContext(channel);
        connectionContext.setCurrentDataBase(statement.getSql());
        return new ExecuteInvokeResult(true);
    }
}
