package com.xiaojiezhu.bigsql.core.schema;

import com.xiaojiezhu.bigsql.common.Reloadable;
import com.xiaojiezhu.bigsql.core.schema.database.LogicDatabase;

import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public interface Schema extends Reloadable {
    /**
     * find the databases
     * @return schema
     */
    Set<String> listDatabaseName();

    /**
     * get the lotic database
     * @param database database name
     * @return logic database
     */
    LogicDatabase getDatabase(String database);

}
