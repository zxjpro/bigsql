package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.InvokeStatementException;
import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.invoker.result.DefaultSelectInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.core.schema.Schema;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * show table
 * time 2018/5/22 18:02
 * @author xiaojie.zhu <br>
 */
public abstract class ShowSingleComponentInvoker extends StatementInvoker {
    public final static Logger LOG = LoggerFactory.getLogger(ShowSingleComponentInvoker.class);
    //public static final String DATABASE_NAME = "Database";
    //public static final int DATABASE_TABLE_NAME_MAX_SIZE= 64;





    public ShowSingleComponentInvoker(Statement statement) {
        super(statement);
    }

    @Override
    public InvokeResult invoke() throws InvokeStatementException {
        BigsqlResultSet resultSet = getDataResultSet();
        return DefaultSelectInvokeResult.createInstance(resultSet);
    }

    /**
     * create result set
     * @return
     */
    protected BigsqlResultSet getDataResultSet() {
        Set<String> databases = this.getData();
        List<Object[]> rowData = null;
        if(databases == null || databases.size() == 0){
            rowData = new ArrayList<>(1);
        }else{
            rowData  = new LinkedList<>();
            for (String database : databases) {
                rowData.add(new Object[]{database});
            }
        }

        Field field = new Field();
        field.setAsName(this.getColumnName());
        field.setColumnDisplaySize(this.getColumnMaxLength());
        field.setName(this.getColumnName());
        ColumnType columnType = this.getColumnType();
        field.setFieldTypeName(columnType.toString());
        field.setFieldType(columnType.getValue());
        return BigsqlResultSet.createInstance(Arrays.asList(field), rowData);
    }

    /**
     * response data
     * @return
     */
    protected abstract Set<String> getData();

    /**
     * column type
     * @return
     */
    protected abstract String getColumnName();

    /**
     * column max length
     * @return
     */
    protected abstract int getColumnMaxLength();

    /**
     * column type
     * @return
     */
    protected abstract ColumnType getColumnType();


}
