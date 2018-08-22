package com.xiaojiezhu.bigsql.server.config;

import com.xiaojiezhu.bigsql.util.BigsqlSystem;
import com.xiaojiezhu.bigsql.util.EnvUtil;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author xiaojie.zhu
 */
public class FileConfigLoader implements ConfigLoader{
    public static final List<String> EXCLUDE_FIELD = Arrays.asList("password");

    public static final String BIGSQL_PROPERTIES_PATH = "bigsql.properties";

    @Override
    public BigSqlConfig loadConfig(String[] args) {
        BigSqlConfig bigSqlConfig = BigSqlConfig.getInstance();
        String bigsqlConfPath = EnvUtil.getBigsqlConfPath();
        File file = new File(bigsqlConfPath + BIGSQL_PROPERTIES_PATH);
        if(!file.exists()){
            BigsqlSystem.exit(file.getAbsolutePath() + " not exists");
            return null;
        }else{
            Properties properties = loadProperties(file);
            reConfig(bigSqlConfig, properties);
            return bigSqlConfig;
        }

    }



    private void reConfig(BigSqlConfig bigSqlConfig , Properties properties){
        Field[] declaredFields = bigSqlConfig.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            String value = properties.getProperty(name);
            if(value != null){
                try {
                    Class<?> type = field.getType();
                    field.set(bigSqlConfig , TypeUtil.parseValue(value , type));
                } catch (Throwable e) {
                    e.printStackTrace();
                    BigsqlSystem.exit("read config error , " + e.getMessage());
                }
            }

            if(!EXCLUDE_FIELD.contains(name)){
                try {
                    System.setProperty("bigsql." + name , String.valueOf(field.get(bigSqlConfig)));
                } catch (Exception e) {
                    e.printStackTrace();
                    BigsqlSystem.exit("read config error , " + e.getMessage());
                }
            }
        }
    }


    private Properties loadProperties(File file){
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(inputStream);

            inputStream.close();

            return properties;

        } catch (IOException e) {
            e.printStackTrace();
            BigsqlSystem.exit("init bigsql conf error");
            return null;
        }

    }
}
