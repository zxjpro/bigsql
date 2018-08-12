package com.xiaojiezhu.bigsql.core.configuration.datasource;

import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * time 2018/8/9 15:32
 *
 * @author xiaojie.zhu <br>
 */
public class BigsqlConnection extends DruidPooledConnection {

    private String dataSourceName;


    public BigsqlConnection(DruidConnectionHolder holder, String dataSourceName) {
        super(holder);
        this.dataSourceName = dataSourceName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }
}


