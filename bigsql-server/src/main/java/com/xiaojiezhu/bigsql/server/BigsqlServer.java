package com.xiaojiezhu.bigsql.server;

import com.xiaojiezhu.bigsql.server.bootstrap.Bootstrap;
import com.xiaojiezhu.bigsql.server.bootstrap.netty.DefaultBootstrap;
import com.xiaojiezhu.bigsql.server.config.BigSqlConfig;
import com.xiaojiezhu.bigsql.server.config.ConfigLoader;
import com.xiaojiezhu.bigsql.server.config.FileConfigLoader;
import com.xiaojiezhu.bigsql.util.Asserts;
import com.xiaojiezhu.bigsql.util.EnvUtil;

import java.io.File;
import java.net.URI;

/**
 * main class
 *
 * @author xiaojie.zhu
 */
public class BigsqlServer {

    private static ConfigLoader configLoader = new FileConfigLoader();

    public static void main(String[] args) {
        BigSqlConfig bigSqlConfig = configLoader.loadConfig(args);
        Asserts.notNull(bigSqlConfig, "bigsql config can not be null");

        initLog4j2();
        Bootstrap bootstrap = new DefaultBootstrap();
        bootstrap.startServer(bigSqlConfig);

    }

    private static void initLog4j2() {
        File f = new File(EnvUtil.getBigsqlConfPath() + "log4j2.xml");
        URI fc = f.toURI();
        org.apache.logging.log4j.core.Logger l = (org.apache.logging.log4j.core.Logger) org.apache.logging.log4j.LogManager
                .getLogger(org.apache.logging.log4j.LogManager.ROOT_LOGGER_NAME);
        l.getContext().setConfigLocation(fc);
    }
}
