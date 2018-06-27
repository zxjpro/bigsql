package com.xiaojiezhu.bigsql.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * time 2018/6/27 11:14
 *
 * @author xiaojie.zhu <br>
 */
public class ConnectionTest {

    public static Connection createConnection() throws Exception {
        return ConnectionUtil.getLocalhost();
    }

    public static void main(String[] args) throws Exception {
        List<Connection> connections = new ArrayList<>();
        for(int i = 0 ; i < 500 ; i ++){
            System.out.println(i);
            Connection connection = createConnection();
            connections.add(connection);
        }

        for (Connection connection : connections) {
            connection.close();
        }

    }
}
