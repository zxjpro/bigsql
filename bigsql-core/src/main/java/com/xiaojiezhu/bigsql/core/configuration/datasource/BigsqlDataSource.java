package com.xiaojiezhu.bigsql.core.configuration.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.SQLException;

/**
 * data source
 * @author xiaojie.zhu
 */
public class BigsqlDataSource extends DruidDataSource {
    private String name;

    public BigsqlDataSource(String name) {
        this.name = name;
    }

    public BigsqlDataSource(boolean fairLock, String name) {
        super(fairLock);
        this.name = name;
    }

    @Override
    public BigsqlConnection getConnection() throws SQLException {
        DruidPooledConnection connection = super.getConnection();
        return new BigsqlConnection(connection.getConnectionHolder(),name);
    }

    @Override
    public String toString() {
        return "dataSource [" + name + "]";
    }

    public static BigsqlDataSource create(String name,String url,String username,String password,int maxActive,String driverClassName){
        BigsqlDataSource dataSource = new BigsqlDataSource(name);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    public static BigsqlDataSource create(DataSourceConfig dataSourceConfig){
        BigsqlDataSource ds = new BigsqlDataSource(dataSourceConfig.getName());
        ds.setUrl(dataSourceConfig.getUrl());
        ds.setUsername(dataSourceConfig.getUsername());
        ds.setPassword(dataSourceConfig.getPassword());
        ds.setDriverClassName(dataSourceConfig.getDriverClassName());
        ds.setMaxActive(dataSourceConfig.getMaxActive());
        ds.setInitialSize(dataSourceConfig.getInitialSize());
        ds.setMaxWait(dataSourceConfig.getMaxWait());
        return ds;
    }
}
