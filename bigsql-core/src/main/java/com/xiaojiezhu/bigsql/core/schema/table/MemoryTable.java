package com.xiaojiezhu.bigsql.core.schema.table;

import com.xiaojiezhu.bigsql.model.construct.Field;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public interface MemoryTable extends LogicTable {

    /**
     * the column name
     * @return
     */
    Set<String> listColumnName();


    /**
     * column detail
     * @return
     */
    List<Field> listColumnFields();

    /**
     * get result set
     * @return
     */
    ResultSet getResultSet();
}
