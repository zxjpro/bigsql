package com.xiaojiezhu.bigsql.util;

import java.io.File;

/**
 * time 2018/8/16 17:33
 *
 * @author xiaojie.zhu <br>
 */
public class EnvUtil {
    private static final String CONF_PATH_NAME = "bigsql.conf";

    public static String getBigsqlConfPath(){
        String confPath = System.getProperty(CONF_PATH_NAME);
        if(confPath == null){
            BigsqlSystem.exit(CONF_PATH_NAME + " property not found");
            return null;
        }else{
            if(!confPath.endsWith(File.separator)){
                confPath = confPath + File.separator;
            }
            return confPath;
        }
    }
}
