package com.xiaojiezhu.bigsql.core.configuration.datasource;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.*;

/**
 * @author xiaojie.zhu
 */
public class DefaultDataSourcePool implements DataSourcePool {
    public final static Logger LOG = LoggerFactory.getLogger(DefaultDataSourcePool.class);
    private final DataSourceLoader dataSourceLoader;

    private Map<String,DataSource> data;



    public DefaultDataSourcePool(String confDirPath) {
        dataSourceLoader = new FileConfigDataSourceLoader(confDirPath);

        try {
            this.reload();
        } catch (Exception e) {
            throw new BigSqlException(100,"init datasource pool fail",e);
        }
    }

    @Override
    public Map<String, DataSource> getDataSource() {
        return (Map<String, DataSource>) data;
    }

    @Override
    public DataSource getDataSource(String name) {
        if(data != null && data.size() > 0){
            DataSource dataSource = data.get(name);
            if(dataSource != null){
                return dataSource;
            }
        }
        throw new NullPointerException("not found '["+name+"]' dataSource");
    }

    @Override
    public void reload()throws Exception {
        synchronized (DefaultDataSourcePool.class){
            Set<String> dataSourceNames = null;
            if(this.data != null){
                dataSourceNames = this.data.keySet();
            }
            Map<String, DataSource> dataSourceMap = dataSourceLoader.load(dataSourceNames);
            if(this.data == null){
                LOG.info("init dataSource : " + dataSourceMap.keySet());
                this.data = dataSourceMap;
            }else{
                if(dataSourceMap != null && dataSourceMap.size() > 0){
                    LOG.info("add dataSource : "+ dataSourceMap.keySet());
                    this.data.putAll(dataSourceMap);
                }

            }
        }
    }

    @Override
    public Set<DataSource> listDataSource() {
        if(data == null){
            return null;
        }else{
            Set<DataSource> dataSourceList = new HashSet<>();
            for(Map.Entry<String,DataSource> entry : data.entrySet()){
                dataSourceList.add(entry.getValue());
            }
            return dataSourceList;
        }
    }
}
