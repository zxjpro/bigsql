package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.field.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.SimpleField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SortField;

import java.util.List;

/**
 * a abstract select command,
 * <pre>
 *     select @@xx,
 *     select id,name from user
 * </pre>
 * @author xiaojie.zhu
 */
public interface CommandSelectStatement extends Statement{

    /**
     * get the select field
     * @return alias field
     */
    List<AliasField> getQueryField();

    /**
     * get the sql group by field
     * @return
     */
    SimpleField getGroupField();

    /**
     * get the sql order by field
     * @return
     */
    SortField getOrderField();



    /**
     * is the sql statement force read master database
     * @return true or false
     */
    boolean isReadMaster();
}
