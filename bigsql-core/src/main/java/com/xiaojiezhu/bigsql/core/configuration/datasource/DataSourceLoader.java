package com.xiaojiezhu.bigsql.core.configuration.datasource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
interface DataSourceLoader {

    /**
     * reload the dataSource list
     * @param existsDataSourceNames exists dataSource name
     * @return name : dataSource
     */
    Map<String,DataSource> load(Set<String> existsDataSourceNames) throws Exception;
}
