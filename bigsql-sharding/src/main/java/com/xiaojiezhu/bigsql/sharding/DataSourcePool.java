package com.xiaojiezhu.bigsql.sharding;

import com.xiaojiezhu.bigsql.common.Reloadable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * it can't update dataSource in the poll,just add new dataSource
 * @author xiaojie.zhu
 */
public interface DataSourcePool extends Reloadable {

    /**
     * get all of dataSource
     * @return
     */
    Map<String,DataSource> getDataSource();

    /**
     * get dataSource by name
     * @param name dataSource name
     * @return dataSource
     */
    DataSource getDataSource(String name);



    /**
     * get the connection list
     * @return connection
     */
    Set<DataSource> listDataSource();

}
