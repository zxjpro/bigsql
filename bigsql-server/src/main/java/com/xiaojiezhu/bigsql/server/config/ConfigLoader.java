package com.xiaojiezhu.bigsql.server.config;

/**
 * @author xiaojie.zhu
 */
public interface ConfigLoader {

    /**
     * reload global config
     * @param args
     * @return
     */
    BigSqlConfig loadConfig(String args[]);
}
