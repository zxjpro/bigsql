package com.xiaojiezhu.bigsql.core.schema.database;

import com.xiaojiezhu.bigsql.core.schema.table.LogicTable;

import java.util.Map;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public class SimpleLogicDatabase extends LogicDatabase {
    protected Map<String,LogicTable> tables;

    public SimpleLogicDatabase(String database) {
        super(database);
    }


    @Override
    public Set<String> listTableName() {
        if(tables != null){
            return tables.keySet();
        }else {
            return null;
        }
    }

    @Override
    public LogicTable getTable(String tableName) {
        return tables.get(tableName);
    }

    public void setTables(Map<String, LogicTable> tables) {
        this.tables = tables;
    }
}
