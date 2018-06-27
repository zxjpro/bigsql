package com.xiaojiezhu.bigsql.server.config;

/**
 * @author xiaojie.zhu
 */
public class DefaultConfigLoader implements ConfigLoader {

    @Override
    public BigSqlConfig loadConfig(String[] args) {
        BigSqlConfig bigSqlConfig = BigSqlConfig.getInstance();
        return bigSqlConfig;
    }
}
