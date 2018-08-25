package com.xiaojiezhu.bigsql.core.schema.table.bigsql;

import com.xiaojiezhu.bigsql.core.BigsqlResultSet;
import com.xiaojiezhu.bigsql.core.configuration.datasource.BigsqlDataSource;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.schema.table.MemoryTable;
import com.xiaojiezhu.bigsql.core.type.Type;
import com.xiaojiezhu.bigsql.core.type.TypeFactory;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.construct.Field;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

/**
 * the bigsql dataSource pool connection mysql instance desc
 * @author xiaojie.zhu
 */
public class ConnectionPoolTable implements MemoryTable {

    private static final List<Field> FIELDS;
    private static final Set<String> COLUMN_FIELDS;

    public static final String DATA_SOURCE_NAME = "dataSourceName";

    public static final String ACTIVE_COUNT = "activeCount";

    public static final String ACTIVE_PEAK = "activePeak";

    public static final String CREATE_COUNT = "createCount";

    public static final String MAX_ACTIVE = "maxActive";

    static {
        FIELDS = new LinkedList<>();
        FIELDS.add(Field.createField(DATA_SOURCE_NAME,20, ColumnType.VARCHAR));
        FIELDS.add(Field.createField(ACTIVE_COUNT,18, ColumnType.BIGINT));
        FIELDS.add(Field.createField(ACTIVE_PEAK,18, ColumnType.BIGINT));
        FIELDS.add(Field.createField(CREATE_COUNT,18, ColumnType.BIGINT));
        FIELDS.add(Field.createField(MAX_ACTIVE,18, ColumnType.BIGINT));

        COLUMN_FIELDS = new HashSet<>();
        COLUMN_FIELDS.add(DATA_SOURCE_NAME);
        COLUMN_FIELDS.add(ACTIVE_COUNT);
        COLUMN_FIELDS.add(ACTIVE_PEAK);
        COLUMN_FIELDS.add(CREATE_COUNT);
        COLUMN_FIELDS.add(MAX_ACTIVE);
    }



    private final String tableName;
    private final String databaseName;
    private final BigsqlContext bigsqlContext;


    public ConnectionPoolTable(String tableName, String databaseName, BigsqlContext bigsqlContext) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.bigsqlContext = bigsqlContext;
    }

    @Override
    public Set<String> listColumnName() {
        return COLUMN_FIELDS;
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

    private List<Type[]> getRowData(){
        List<Type[]> rowData = new LinkedList<>();
        DataSourcePool dataSourcePool = bigsqlContext.getDataSourcePool();
        Map<String, DataSource> dataSources = dataSourcePool.getDataSource();
        Iterator<Map.Entry<String, DataSource>> iterator = dataSources.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, DataSource> entry = iterator.next();
            DataSource dataSource = entry.getValue();
            BigsqlDataSource ds = (BigsqlDataSource) dataSource;

            Type[] row = new Type[COLUMN_FIELDS.size()];
            row[0] = TypeFactory.getType(entry.getKey());
            row[1] = TypeFactory.getType(ds.getActiveCount());
            row[2] = TypeFactory.getType(ds.getActivePeak());
            row[3] = TypeFactory.getType(ds.getCreateCount());
            row[4] = TypeFactory.getType(ds.getMaxActive());

            rowData.add(row);
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
