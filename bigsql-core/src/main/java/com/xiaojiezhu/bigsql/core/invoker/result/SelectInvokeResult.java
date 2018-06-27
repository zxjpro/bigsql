package com.xiaojiezhu.bigsql.core.invoker.result;

import java.sql.ResultSet;

/**
 * invoke select command,the response data
 * @author xiaojie.zhu
 */
public interface SelectInvokeResult extends InvokeResult {

    /**
     * get the select result set
     * @return if success,the must can not be null
     */
    ResultSet getResultSet();

}
