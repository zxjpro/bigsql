package com.xiaojiezhu.bigsql.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.xiaojiezhu.bigsql.util.IOUtil;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * time 2018/6/27 11:55
 *
 * @author xiaojie.zhu <br>
 */
public class ConnectionUtil {

    public static Connection getLocalhost() throws Exception {
        return loadConnection(new File("D:\\conn\\local.txt"));
    }

    public static Connection getIdc49() throws Exception {
        return loadConnection(new File("D:\\conn\\idc49.txt"));
    }

    public static Connection get222() throws Exception {
        return loadConnection(new File("D:\\conn\\222.txt"));
    }

    public static Connection getLocalhostStation() throws Exception {
        return loadConnection(new File("D:\\conn\\local_station.txt"));
    }

    public static DataSource getLocalhostStationDataSource() throws Exception {
        return loadConnectionDataSource(new File("D:\\conn\\local_station.txt"));
    }

    /**
     * 虚拟机
     * @return
     */
    public static Connection getVir233() {
        return null;
    }



    private static Connection loadConnection(File file) throws Exception {
        BufferedReader reader = null;
        String jdbcUrl;
        String username;
        String password;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            jdbcUrl = reader.readLine();
            username = reader.readLine();
            password = reader.readLine();
        } finally {
            IOUtil.close(reader);
        }

        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        return connection;
    }


    private static DruidDataSource loadConnectionDataSource(File file) throws Exception {
        BufferedReader reader = null;
        String jdbcUrl;
        String username;
        String password;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            jdbcUrl = reader.readLine();
            username = reader.readLine();
            password = reader.readLine();
        } finally {
            IOUtil.close(reader);
        }

        Class.forName("com.mysql.jdbc.Driver");
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        return ds;
    }

}
