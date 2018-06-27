package com.xiaojiezhu.bigsql.server;

import com.xiaojiezhu.bigsql.server.bootstrap.Bootstrap;
import com.xiaojiezhu.bigsql.server.bootstrap.netty.DefaultBootstrap;
import com.xiaojiezhu.bigsql.server.config.BigSqlConfig;
import com.xiaojiezhu.bigsql.server.config.ConfigLoader;
import com.xiaojiezhu.bigsql.server.config.DefaultConfigLoader;
import com.xiaojiezhu.bigsql.util.Asserts;

/**
 * main class
 * @author xiaojie.zhu
 */
public class BigsqlServer {
    private static final String DEFAULT_CONFIG_PATH = "config/bigsql.properties";
    // TODO update config source
    private static ConfigLoader configLoader = new DefaultConfigLoader();

    public static void main(String[] args)  {
        BigSqlConfig bigSqlConfig = configLoader.loadConfig(args);
        Asserts.notNull(bigSqlConfig,"bigsql config can not be null");

        // TODO: reload log4j2.xml to other path
        Bootstrap bootstrap = new DefaultBootstrap();
        bootstrap.startServer(bigSqlConfig);

    }
}
