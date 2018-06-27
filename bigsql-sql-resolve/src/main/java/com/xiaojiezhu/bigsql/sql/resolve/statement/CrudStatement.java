package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.xiaojiezhu.bigsql.sql.resolve.CrudType;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
public interface CrudStatement extends Statement {

    /**
     * get the statement tables
     * @return tables
     */
    List<TableName> getTables();

    /**
     * get crud type
     * @return
     */
    CrudType getCrudType();

}
