package com.xiaojiezhu.bigsql.core.schema.database;

import com.xiaojiezhu.bigsql.core.schema.table.LogicTable;

import java.util.List;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public abstract class LogicDatabase {
    /**
     * database name
     */
    protected String database;

    public LogicDatabase(String database) {
        this.database = database;
    }






    /**
     * list the database table name
     * @return table list
     */
    public abstract Set<String> listTableName();

    /**
     * get the table
     * @param tableName table name
     * @return logic table
     */
    public abstract LogicTable getTable(String tableName);

    @Override
    public String toString() {
        return "database : " + database;
    }
}
