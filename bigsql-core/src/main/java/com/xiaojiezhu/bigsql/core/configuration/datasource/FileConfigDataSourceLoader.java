package com.xiaojiezhu.bigsql.core.configuration.datasource;

import com.xiaojiezhu.bigsql.common.exception.DataSourceLoadException;
import com.xiaojiezhu.bigsql.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * reload dataSource by dir file
 * @author xiaojie.zhu
 */
class FileConfigDataSourceLoader implements DataSourceLoader {
    private static final String DATASOURCE_DIR = "datasource";
    private static final String NAME = "name";
    private static final String URL = "url";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String DRIVER_CLASS_NAME = "driverClassName";
    private static final String MAX_ACTIVE = "maxActive";
    private static final String INITIAL_SIZE = "initialSize";
    private static final String MAX_WAIT = "maxWait";
    final static Logger LOG = LoggerFactory.getLogger(FileConfigDataSourceLoader.class);
    /**
     * the config dir,reload the all file from the dir
     */
    protected String dataSourceConfigDir;

    public FileConfigDataSourceLoader(String dataSourceConfigDir) {
        this.dataSourceConfigDir = dataSourceConfigDir + DATASOURCE_DIR;
    }

    @Override
    public Map<String, DataSource> load(Set<String> existsDataSourceNames)throws Exception {
        File file = new File(dataSourceConfigDir);
        if(!file.exists()){
            String msg = "reload dataSource fail , " + dataSourceConfigDir + " not exists";
            throw new FileNotFoundException(msg);
        }else{
            File[] files = file.listFiles();
            if(files != null && files.length > 0){
                List<DataSourceConfig> dataSourceConfigs = new ArrayList<>();
                for (File f : files) {
                    if(f.isDirectory()){
                        LOG.warn("reload dataSource config , find a directory , bigsql not reload : " + f.getAbsolutePath());
                    }else{
                        DataSourceConfig dataSourceConfig = createDataSourceConfig(f);
                        if(existsDataSourceNames != null && existsDataSourceNames.size() > 0){
                            if(!existsDataSourceNames.contains(dataSourceConfig.getName())){
                                dataSourceConfigs.add(dataSourceConfig);
                            }
                        }else {
                            dataSourceConfigs.add(dataSourceConfig);
                        }
                    }
                }
                checkDataSourceConfig(dataSourceConfigs);
                Map<String, DataSource> dataSourceMap = createDataSource(dataSourceConfigs);
                return dataSourceMap;
            }else{
                LOG.warn("not found dataSource config on : " + dataSourceConfigDir);
                return null;
            }
        }
    }

    /**
     * create dataSourceMap by configList
     * @param dataSourceConfigs configList
     * @return dataSourceMap
     */
    protected Map<String,DataSource> createDataSource(List<DataSourceConfig> dataSourceConfigs){
        if(dataSourceConfigs == null || dataSourceConfigs.size() == 0){
            return null;
        }else{
            Map<String,DataSource> dataSourceMap = new HashMap<>(dataSourceConfigs.size());
            for (DataSourceConfig dataSourceConfig : dataSourceConfigs) {
                dataSourceMap.put(dataSourceConfig.getName(),BigsqlDataSource.create(dataSourceConfig));
            }
            return dataSourceMap;
        }
    }




    /**
     * check dataSource config , it name repetition , will throw exception
     * @param dataSourceConfigs dataSourceConfigs
     */
    protected void checkDataSourceConfig(List<DataSourceConfig> dataSourceConfigs){
        if(dataSourceConfigs != null && dataSourceConfigs.size() > 0){
            Set<String> tmp = new HashSet<>();
            for (DataSourceConfig dataSourceConfig : dataSourceConfigs) {
                String name = dataSourceConfig.getName();
                if(tmp.contains(name)){
                    throw new DataSourceLoadException("reload dataSource fail , the dataSource name must be single , but there is not : " + name);
                }
                tmp.add(name);
            }
        }
    }

    protected DataSourceConfig createDataSourceConfig(File file) throws IOException {
        FileInputStream inputStream = null;
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        try {
            inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);

            String name = file.getName();
            name = name.substring(0,name.indexOf("."));
            dataSourceConfig.setName(name);
            String url = loadValue(properties,URL,true);
            dataSourceConfig.setUrl(url);
            String username = loadValue(properties,USERNAME,true);
            dataSourceConfig.setUsername(username);
            String password = loadValue(properties,PASSWORD,true);
            dataSourceConfig.setPassword(password);
            String driverClassName = loadValue(properties,DRIVER_CLASS_NAME,true);
            dataSourceConfig.setDriverClassName(driverClassName);

            String maxActive = properties.getProperty(MAX_ACTIVE);
            if(maxActive != null){
                dataSourceConfig.setMaxActive(Integer.parseInt(maxActive));
            }
            String initialSize = properties.getProperty(INITIAL_SIZE);
            if(initialSize != null){
                dataSourceConfig.setInitialSize(Integer.parseInt(initialSize));
            }
            String maxWait = properties.getProperty(MAX_WAIT);
            if(maxWait != null){
                dataSourceConfig.setMaxWait(Integer.parseInt(maxWait));
            }
        }finally {
            IOUtil.close(inputStream);
        }

        return dataSourceConfig;
    }

    private String loadValue(Properties properties,String name,boolean required){
        String value = properties.getProperty(name);
        if(value == null){
            if(required){
                throw new NullPointerException("reload dataSource fail , "+name+" can not be null");
            }
        }
        return value;
    }
}
