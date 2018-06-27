package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.configuration.Entry;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;


/**
 * gen key-value response,
 *
 * like 'show status,show variables'
 *
 * <pre>
 * --------------------------------------------
 * -       Variable_name       -    Value     -
 * --------------------------------------------
 * - auto_increment_increment  -      1       -
 * --------------------------------------------
 * -  avoid_temporal_upgrade   -     OFF      -
 * --------------------------------------------
 * -    binlog_error_action    - ABORT_SERVER -
 * --------------------------------------------
 * </pre>
 *
 * @author xiaojie.zhu
 */
public abstract class AbstractKeyValueQueryStatementInvoker extends StatementInvoker {
    public final static Logger LOG = LoggerFactory.getLogger(AbstractKeyValueQueryStatementInvoker.class);
    private DefaultSelectInvokeResult result;
    protected final BigsqlConfiguration configuration;

    public AbstractKeyValueQueryStatementInvoker(Statement statement,BigsqlConfiguration configuration) {
        super(statement);
        this.configuration = configuration;
    }


    @Override
    public InvokeResult invoke()throws InvokeStatementException {
        synchronized (this){
            if(result == null){
                List<Field> fields = this.getQueryFields();
                List<String> keys = getKeys();
                List<Object[]> rowData = new LinkedList<>();
                for(int i = 0 ; i < keys.size() ; i ++){
                    String key = keys.get(i);
                    Object[] row = new Object[fields.size()];
                    Entry entry = this.getValue(key);
                    if(entry != null){
                        row[0] = key;
                        row[1] = entry.getValue();
                    }
                    rowData.add(row);
                }


                ResultSet resultSet = BigsqlResultSet.createInstance(fields,rowData);
                this.result = DefaultSelectInvokeResult.createInstance(resultSet);
            }

            return this.result;
        }
    }

    /**
     * get query field names
     * @return
     */
    protected abstract List<Field> getQueryFields();

    /**
     * get the keys,on column 1
     * @return
     */
    protected abstract List<String> getKeys();

    /**
     * get the key value,this is a keyValuePair,
     * @param fieldName
     * @return
     */
    protected abstract Entry getValue(String fieldName);

}
