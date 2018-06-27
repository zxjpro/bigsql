package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;

import java.util.Arrays;

/**
 * @author xiaojie.zhu
 */
public class NullResultInvoker extends StatementInvoker {
    public NullResultInvoker(Statement statement) {
        super(statement);
    }

    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        Field field = new Field();
        field.setFieldTypeName(ColumnType.VARCHAR.toString());
        field.setFieldType(ColumnType.VARCHAR.getValue());
        field.setName("null");
        field.setColumnDisplaySize(6);
        field.setAsName("null");
        BigsqlResultSet resultSet = BigsqlResultSet.createInstance(Arrays.asList(field), null);
        return DefaultSelectInvokeResult.createInstance(resultSet);
    }
}
