package com.xiaojiezhu.bigsql.test;

import java.sql.*;

/**
 * time 2018/6/26 17:26
 *
 * @author xiaojie.zhu <br>
 */
public class ConcurrentTest {

    public static void main(String[] args) throws Exception {
        Connection connection = createConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        String sql = "insert into person(name,age,create_time) values('999',999,now())";
        ResultSet resultSet = statement.executeQuery(sql);
        connection.commit();
    }

    public static void insert() throws Exception {
        int threadNum = 20;
        for (int i = 0 ; i < threadNum ; i ++){
            Connection connection = createConnection();
            Thread thread = new Thread(new Insert(i , connection));
            thread.setName("test-" + i);
            thread.start();
        }
    }

    public static Connection createConnection() throws Exception {
        return ConnectionUtil.get222();
    }

    public static class Insert implements Runnable{
        private Connection connection;
        private int i;

        public Insert(int i , Connection connection) {
            this.i = i;
            this.connection = connection;
        }

        @Override
        public void run() {
            for(int j = 0 ; j < 100000 ; j ++){
                String sql = "insert into person(name,age,create_time) values(?,"+j+",now())";
                try {
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1 , "name-" + this.i + "-" + j);
                    int i = statement.executeUpdate();
                    if(i != 1){
                        System.out.println("errrr");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

