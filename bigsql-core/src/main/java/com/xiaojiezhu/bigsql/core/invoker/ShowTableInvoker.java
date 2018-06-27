package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.schema.database.LogicDatabase;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import io.netty.channel.Channel;

import java.util.Set;

/**
 * show table
 * @author xiaojie.zhu
 */
public class ShowTableInvoker extends ShowSingleComponentInvoker {
    private String dataBase;
    public static final String COLUMN_NAME = "Tables_in_mysql";
    public static final int TABLE_NAME_MAX_LENGTH = 64;
    protected BigsqlContext context;


    public ShowTableInvoker(Statement statement, String dataBase , BigsqlContext context) {
        super(statement);
        this.dataBase = dataBase;
        this.context = context;
    }


    @Override
    protected Set<String> getData() {
        LogicDatabase database = context.getSchema().getDatabase(dataBase);
        return database.listTableName();
    }

    @Override
    protected String getColumnName() {
        return COLUMN_NAME;
    }

    @Override
    protected int getColumnMaxLength() {
        return TABLE_NAME_MAX_LENGTH;
    }

    @Override
    protected ColumnType getColumnType() {
        return ColumnType.VARCHAR;
    }
}
