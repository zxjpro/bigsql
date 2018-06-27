package com.xiaojiezhu.bigsql.core.merge;

import com.xiaojiezhu.bigsql.common.exception.MergeException;

import java.sql.ResultSet;

/**
 * @author xiaojie.zhu
 */
public interface Merge {

    ResultSet merge()throws MergeException;
}
