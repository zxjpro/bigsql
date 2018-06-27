package com.xiaojiezhu.bigsql.server.bootstrap;

import com.xiaojiezhu.bigsql.server.config.BigSqlConfig;

import java.io.Closeable;

/**
 * @author xiaojie.zhu
 */
public interface Bootstrap extends Closeable{

    /**
     * start bigsql server
     * @param bigSqlConfig
     */
    void startServer(BigSqlConfig bigSqlConfig);

}
