package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.core.type.TypeFactory;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.statement.ShowCreateTableStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;

import java.util.ArrayList;
import java.util.List;

/**
 * SHOW CREATE TABLE 'tableName'
 * @author xiaojie.zhu
 */
public class ShowCreateTableInvoker extends StatementInvoker {
    private List<AliasField> fields;

    public ShowCreateTableInvoker(Statement statement) {
        super(statement);
    }


    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        if(statement instanceof ShowCreateTableStatement){
            ShowCreateTableStatement createTableStatement = (ShowCreateTableStatement) statement;
            String tableName = createTableStatement.getTableName();
            List<Field> fields = new ArrayList<>(2);
            fields.add(Field.createField("TableName",64, ColumnType.VARCHAR));
            fields.add(Field.createField("Create Table",2048,ColumnType.VARCHAR));

            List<Type[]> rowData = new ArrayList<>(1);
            rowData.add(TypeFactory.getType( tableName,""));

            BigsqlResultSet resultSet = BigsqlResultSet.createInstance(fields, rowData);
            return DefaultSelectInvokeResult.createInstance(resultSet);
        }else{
            throwNotSupport();
            return null;
        }
    }


}
