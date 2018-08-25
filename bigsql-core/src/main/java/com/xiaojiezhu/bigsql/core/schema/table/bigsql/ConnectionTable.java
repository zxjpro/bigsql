package com.xiaojiezhu.bigsql.core.schema.table.bigsql;

import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.context.ConnectionContext;
import com.xiaojiezhu.bigsql.core.schema.table.MemoryTable;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.core.type.TypeFactory;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * desc connection bigsql connection's
 * @author xiaojie.zhu
 */
public class ConnectionTable implements MemoryTable {

    public static final String ID = "Id";

    public static final String USER = "User";

    public static final String HOST = "Host";

    public static final String DB = "db";

    public static final String COMMAND = "Command";

    public static final String TIME = "Time";

    public static final String STATE = "State";

    public static final String INFO = "Info";

    private final String tableName;
    private final String databaseName;
    private final BigsqlContext bigsqlContext;

    private static final List<Field> FIELDS;
    private static final Set<String> COLUMN_NAMES;



    static {
        FIELDS = new LinkedList<>();
        FIELDS.add(Field.createField(ID,15, ColumnType.INT));
        FIELDS.add(Field.createField(USER,10, ColumnType.VARCHAR));
        FIELDS.add(Field.createField(HOST,20, ColumnType.VARCHAR));
        FIELDS.add(Field.createField(DB,20, ColumnType.VARCHAR));
        FIELDS.add(Field.createField(COMMAND,20, ColumnType.VARCHAR));
        FIELDS.add(Field.createField(TIME,18, ColumnType.BIGINT));
        FIELDS.add(Field.createField(STATE,10, ColumnType.VARCHAR));
        FIELDS.add(Field.createField(INFO,512, ColumnType.VARCHAR));

        COLUMN_NAMES = new HashSet<>();
        COLUMN_NAMES.add(ID);
        COLUMN_NAMES.add(USER);
        COLUMN_NAMES.add(HOST);
        COLUMN_NAMES.add(DB);
        COLUMN_NAMES.add(COMMAND);
        COLUMN_NAMES.add(TIME);
        COLUMN_NAMES.add(STATE);
        COLUMN_NAMES.add(INFO);

    }




    public ConnectionTable(String tableName, String databaseName , BigsqlContext bigsqlContext) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.bigsqlContext = bigsqlContext;
    }

    @Override
    public Set<String> listColumnName() {
        return COLUMN_NAMES;
    }

    @Override
    public List<Field> listColumnFields() {
        return FIELDS;
    }

    @Override
    public ResultSet getResultSet() {
        List<Type[]> rowData = getRowData();

        BigsqlResultSet resultSet = BigsqlResultSet.createInstance(FIELDS, rowData);
        return resultSet;
    }

    private List<Type[]> getRowData() {
        List<ConnectionContext> connectionContexts = bigsqlContext.getConnectionContexts();
        List<Type[]> rowData = new LinkedList<>();
        if(connectionContexts != null && connectionContexts.size() > 0){
            for (ConnectionContext connectionContext : connectionContexts) {
                Type[] row = new Type[FIELDS.size()];
                row[0] = TypeFactory.getType(connectionContext.getConnectionId());
                row[1] = TypeFactory.getType(connectionContext.getUserName());
                row[2] = TypeFactory.getType(connectionContext.getHost());
                row[3] = TypeFactory.getType(connectionContext.getCurrentDataBase());
                row[4] = null;
                row[5] = TypeFactory.getType((int) ((System.currentTimeMillis() - connectionContext.getConnectionTime()) / 1000));
                row[6] = null;
                row[7] = TypeFactory.getType(connectionContext.getCurrentStatement().getSql());
                rowData.add(row);
            }
        }
        return rowData;
    }

    @Override
    public String getName() {
        return this.tableName;
    }

    @Override
    public String getDatabaseName() {
        return this.databaseName;
    }
}
