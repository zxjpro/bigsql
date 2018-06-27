package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.configuration.Entry;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * response a resultSet, handler something simple query
 *
 * @author xiaojie.zhu
 */
public abstract class AbstractQueryStatementInvoker extends StatementInvoker {
    public final static Logger LOG = LoggerFactory.getLogger(AbstractQueryStatementInvoker.class);
    private DefaultSelectInvokeResult result;
    protected final BigsqlConfiguration bigsqlConfiguration;
    private List<AliasField> aliasFields;


    public AbstractQueryStatementInvoker(Statement statement,BigsqlConfiguration bigsqlConfiguration) {
        super(statement);
        this.bigsqlConfiguration = bigsqlConfiguration;
    }



    @Override
    public InvokeResult invoke()throws InvokeStatementException {
        synchronized (this){
            if(result == null){
                List<AliasField> queryField = this.getQueryFields();

                List<Field> fields = new ArrayList<>(queryField.size());
                List<Object[]> rowData = new LinkedList<>();
                for(int i = 0 ; i < queryField.size() ; i ++){
                    Object[] row = new Object[queryField.size()];
                    AliasField aliasField = queryField.get(i);
                    Field field = new Field();
                    field.setAsName(aliasField.getAsName());
                    field.setColumnDisplaySize(getColumnMaxSize(aliasField.getName()));

                    field.setName(aliasField.getName());

                    Entry entry = this.getValue(aliasField.getName());
                    if(entry != null){
                        field.setFieldType(entry.getColumnType().getValue());
                        field.setFieldTypeName(entry.getColumnType().toString());
                        row[i] = entry.getValue();
                    }
                    fields.add(field);

                    rowData.add(row);
                }


                ResultSet resultSet = BigsqlResultSet.createInstance(fields,rowData);
                this.result = DefaultSelectInvokeResult.createInstance(resultSet);
            }

            return this.result;
        }
    }

    protected abstract List<AliasField> getQueryFields();

    protected abstract Entry getValue(String fieldName);

    /**
     * get the column max length
     * @return
     */
    protected abstract int getColumnMaxSize(String fieldName);
}
