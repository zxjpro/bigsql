package com.xiaojiezhu.bigsql.util;

/**
 * time 2018/5/24 11:03
 *
 * @author xiaojie.zhu <br>
 */
public class BigsqlSystem {


    public static void exit(String msg){
        System.err.println("============================================================");
        System.err.println(msg);
        System.err.println("bigsql will exit");
        System.err.println("============================================================");
        System.exit(0);
    }

    public static String getProperty(String key) {
        return System.getProperty(key);
    }
}
