package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.configuration.Entry;
import com.xiaojiezhu.bigsql.core.context.ConnectionContext;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.core.type.TypeFactory;
import com.xiaojiezhu.bigsql.core.type.VarcharType;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.SimpleSelectStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * the bigsql environment
 *
 * <pre>
 *     select @@xx,@xxb
 * </pre>
 *
 * @author xiaojie.zhu
 */
public class EnvironmentStatementInvoker extends StatementInvoker {
    public final static Logger LOG = LoggerFactory.getLogger(EnvironmentStatementInvoker.class);

    public static final String SELECT_DATABASES = "SELECT DATABASE()";
    public static final String SELECT_DATABASES_FIELD_NAME = "DATABASE()";


    private final BigsqlConfiguration configuration;
    private DefaultSelectInvokeResult result;
    public static final int ENVIRONMENT_MAX_SIZE = 64;
    private ConnectionContext connectionContext;

    public EnvironmentStatementInvoker(Statement statement, BigsqlConfiguration configuration, ConnectionContext connectionContext) {
        super(statement);
        this.configuration = configuration;
        this.connectionContext = connectionContext;
    }


    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        if(statement.getSql().equalsIgnoreCase(SELECT_DATABASES)){
            String currentDataBase = connectionContext.getCurrentDataBase();

            Field field = Field.createField(SELECT_DATABASES_FIELD_NAME, ENVIRONMENT_MAX_SIZE, ColumnType.VARCHAR);
            List<Type[]> rowData = new LinkedList<>();
            rowData.add(new Type[]{new VarcharType(currentDataBase)});
            BigsqlResultSet resultSet = BigsqlResultSet.createInstance(Arrays.asList(field), rowData);
            this.result = DefaultSelectInvokeResult.createInstance(resultSet);
            return this.result;
        }else{
            return invokeEnv();
        }

    }


    private InvokeResult invokeEnv() {
        synchronized (this) {
            if (this.result == null) {
                if (this.statement instanceof SimpleSelectStatement) {
                    SimpleSelectStatement simpleSelectStatement = (SimpleSelectStatement) statement;
                    List<AliasField> queryField = simpleSelectStatement.getQueryField();
                    List<Type[]> rowData = new LinkedList<>();
                    Type[] row = new Type[queryField.size()];
                    List<Field> fields = new ArrayList<>(queryField.size());
                    for (int i = 0; i < queryField.size(); i++) {
                        AliasField aliasField = queryField.get(i);
                        Entry entry = configuration.getEnvironment(aliasField.getName());

                        Field field = new Field();
                        field.setAsName(aliasField.getAsName());
                        field.setColumnDisplaySize(ENVIRONMENT_MAX_SIZE);
                        field.setName(aliasField.getName());
                        field.setFieldType(entry.getColumnType().getValue());
                        field.setFieldTypeName(entry.getColumnType().toString());

                        fields.add(field);
                        row[i] = TypeFactory.getType(entry.getValue());
                    }
                    rowData.add(row);

                    BigsqlResultSet resultSet = BigsqlResultSet.createInstance(fields, rowData);
                    this.result = DefaultSelectInvokeResult.createInstance(resultSet);
                } else {
                    throwNotSupport();
                }
            }


            return result;
        }
    }
}
