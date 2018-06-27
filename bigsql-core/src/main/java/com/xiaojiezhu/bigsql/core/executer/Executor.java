package com.xiaojiezhu.bigsql.core.executer;

import com.xiaojiezhu.bigsql.sharding.ExecuteBlock;

import java.io.Closeable;
import java.util.List;

/**
 * sql executor
 * @author xiaojie.zhu
 */
public interface Executor<T> extends Closeable {

    /**
     * must execute this method first,
     * @param executeBlocks
     */
    void execute(List<ExecuteBlock> executeBlocks) throws Exception;

    /**
     * must execute 'execute()' method first<br>
     *
     * get execute result
     * @return
     */
    T getResult()throws Exception;




}
