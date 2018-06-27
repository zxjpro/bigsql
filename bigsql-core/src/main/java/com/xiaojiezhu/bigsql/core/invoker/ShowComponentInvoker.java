package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.core.schema.table.MemoryTable;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.sql.resolve.statement.ShowEngineStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;

import java.sql.ResultSet;

/**
 * @author xiaojie.zhu
 */
public class ShowComponentInvoker extends StatementInvoker {
    private BigsqlContext context;

    public ShowComponentInvoker(Statement statement, BigsqlContext context) {
        super(statement);
        this.context = context;
    }

    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        if(statement instanceof ShowEngineStatement){
            MemoryTable table = (MemoryTable) context.getSchema().getDatabase(Constant.DEFAULT_DATABASE_NAME).getTable(Constant.ENGINES_NAME);
            ResultSet resultSet = table.getResultSet();
            return DefaultSelectInvokeResult.createInstance(resultSet);
        }else{
            throwNotSupport();
            return null;
        }
    }
}
