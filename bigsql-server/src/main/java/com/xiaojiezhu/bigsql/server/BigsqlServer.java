package com.xiaojiezhu.bigsql.server;

import com.xiaojiezhu.bigsql.server.bootstrap.Bootstrap;
import com.xiaojiezhu.bigsql.server.bootstrap.netty.DefaultBootstrap;
import com.xiaojiezhu.bigsql.server.config.BigSqlConfig;
import com.xiaojiezhu.bigsql.server.config.ConfigLoader;
import com.xiaojiezhu.bigsql.server.config.FileConfigLoader;
import com.xiaojiezhu.bigsql.util.Asserts;
import com.xiaojiezhu.bigsql.util.EnvUtil;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

        initLog4j2();


    }

    private static void initLog4j2() {
        File log4jFile = new File(EnvUtil.getBigsqlConfPath() + "log4j2.xml");
        try {
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(log4jFile), log4jFile);
            Configurator.initialize(null, source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
