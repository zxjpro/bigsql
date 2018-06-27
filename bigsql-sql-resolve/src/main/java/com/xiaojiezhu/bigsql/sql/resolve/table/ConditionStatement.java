package com.xiaojiezhu.bigsql.sql.resolve.table;

import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;

import java.util.List;

/**
 * has condition sql command,
 * UPDATE,DELETE,SELECT must implement this interface
 * @author xiaojie.zhu
 */
public interface ConditionStatement extends Statement {


    /**
     * get the condition fields
     * <pre>
     *     where id=1 and name='kangkang'
     * </pre>
     * @return
     */
    List<ConditionField> getConditionFields();
}
