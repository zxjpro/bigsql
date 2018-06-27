package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.invoker.result.ExecuteInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;

/**
 * @author xiaojie.zhu
 */
public class SettingStatementInvoker extends StatementInvoker {
    public SettingStatementInvoker(Statement statement) {
        super(statement);
    }

    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        return new ExecuteInvokeResult();
    }
}
