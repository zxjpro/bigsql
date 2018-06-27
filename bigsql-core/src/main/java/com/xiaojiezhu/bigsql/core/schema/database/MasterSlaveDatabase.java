package com.xiaojiezhu.bigsql.core.schema.database;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.schema.table.DefaultMasterSlaveTable;
import com.xiaojiezhu.bigsql.core.schema.table.LogicTable;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.rule.masterslave.MasterSlaveRule;
import com.xiaojiezhu.bigsql.util.IOUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * @author xiaojie.zhu
 */
public class MasterSlaveDatabase extends LogicDatabase {
    protected MasterSlaveRule masterSlaveRule;
    protected BigsqlContext context;

    public MasterSlaveDatabase(String database, BigsqlContext context, MasterSlaveRule masterSlaveRule) {
        super(database);
        this.context = context;
        this.masterSlaveRule = masterSlaveRule;
    }

    @Override
    public Set<String> listTableName() {
        Connection connection;
        try {
             connection = getSlaveConnection();
        } catch (SQLException e) {
            throw new BigSqlException(400 , "can not get connection" , e);
        }

        Set<String> tables;
        try {
             tables = showTables(connection);
        } catch (SQLException e) {
            throw new BigSqlException(400 , "show tables fail " , e);
        } finally {
            IOUtil.close(connection);
        }

        return tables;
    }

    private Set<String> showTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SHOW TABLES");
        Set<String> tableNames = new HashSet<>();
        while (resultSet.next()){
            tableNames.add(resultSet.getString(1));
        }
        return tableNames;

    }

    private Connection getSlaveConnection() throws SQLException {
        DataSource dataSource = context.getDataSourcePool().getDataSource(masterSlaveRule.getMasterSlaveDatasource().getSlaveDatasourceName().get(0));
        if(dataSource == null){
            throw new BigSqlException(100 , "can not found dataSource " );
        }
        return dataSource.getConnection();

    }

    @Override
    public LogicTable getTable(String tableName) {
        DefaultMasterSlaveTable table = new DefaultMasterSlaveTable(this.database , tableName , context , masterSlaveRule);
        return table;
    }
}
