package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * show table
 * time 2018/5/22 18:02
 * @author xiaojie.zhu <br>
 */
public class ShowDataBaseInvoker extends ShowSingleComponentInvoker {
    public final static Logger LOG = LoggerFactory.getLogger(ShowDataBaseInvoker.class);
    public static final String DATABASE_NAME = "Database";
    public static final int DATABASE_TABLE_NAME_MAX_SIZE= 64;
    protected BigsqlContext context;

    public ShowDataBaseInvoker(Statement statement, BigsqlContext context) {
        super(statement);
        this.context = context;
    }

    @Override
    protected Set<String> getData() {
        return context.getSchema().listDatabaseName();
    }

    @Override
    protected String getColumnName() {
        return DATABASE_NAME;
    }

    @Override
    protected int getColumnMaxLength() {
        return DATABASE_TABLE_NAME_MAX_SIZE;
    }

    @Override
    protected ColumnType getColumnType() {
        return ColumnType.VARCHAR;
    }


}
