package com.xiaojiezhu.bigsql.test;

import java.sql.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * time 2018/6/26 17:26
 *
 * @author xiaojie.zhu <br>
 */
public class ConcurrentTest {

    static boolean tx = true;
    static AtomicLong count = new AtomicLong();

    public static void main(String[] args) throws Exception {
        insert();
    }


    public static void insert() throws Exception {
        int threadNum = 100;
        for (int i = 0 ; i < threadNum ; i ++){
            Thread thread = new Thread(new Insert());
            thread.setName("thread-" + i);
            thread.start();
        }
    }

    public static Connection createConnection()  {
        try {
            return ConnectionUtil.get222Sharding();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Insert implements Runnable{
        private int txN = 10;

        public Insert() {
        }

        @Override
        public void run() {
            for(int i = 0 ; i < 5000 ; i ++){
                Connection connection = createConnection();
                if(tx){
                    try {
                        connection.setAutoCommit(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String sql = "insert into person(name,age,sex,tel,create_time) values(?,"+count.get()+",?,?,now())";
                PreparedStatement statement = null;
                try {
                    statement = connection.prepareStatement(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                for(int j = 0 ; j < txN ; j++){
                    long val = count.incrementAndGet();

                    System.out.println(val);

                    long start = System.currentTimeMillis();

                    try {

                        statement.setString(1 , Thread.currentThread().getName() + ":" + j);
                        statement.setInt(2, (int) (val % 2));
                        statement.setString(3,"tel:" + val);

                        statement.execute();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    long end = System.currentTimeMillis();

                    System.out.println("=======" + (end - start));
                }

                if(tx){
                    try {
                        connection.commit();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        }
    }
}

