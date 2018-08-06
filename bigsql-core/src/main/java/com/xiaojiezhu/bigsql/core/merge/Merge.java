package com.xiaojiezhu.bigsql.core.merge;

import com.xiaojiezhu.bigsql.common.exception.MergeException;

import java.sql.ResultSet;

/**
 * @author xiaojie.zhu
 */
public interface Merge {

    /**
     * merge the multipart ResultSet
     * @return
     * @throws MergeException
     */
    ResultSet merge()throws MergeException;
}
