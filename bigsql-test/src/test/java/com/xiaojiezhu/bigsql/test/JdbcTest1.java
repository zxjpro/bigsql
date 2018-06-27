package com.xiaojiezhu.bigsql.test;

import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/7 10:15
 * 说明 ...
 */
public class JdbcTest1 {




    private Connection connection;


    @Before
    public void init() throws Exception {
        connection = ConnectionUtil.getLocalhost();
    }


    @Test
    public void getDatabases() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("show databases");
        printlnResult(resultSet);
    }

    @Test
    public void insert() throws SQLException {
        Statement statement = connection.createStatement();
        for(int i = 0 ; i < 200 ; i ++){
            String sql = "INSERT INTO `person` (`name`, `age`, `sex`, `tel`, `create_time`) VALUES ('name"+i+"', '"+i+"', '1', '1', '2018-06-10 10:57:19.0')";
            long startTime = System.currentTimeMillis();
            int n = statement.executeUpdate(sql);
            long endTime = System.currentTimeMillis();
            System.out.println(i + " - " + n + " - " + (endTime - startTime));
        }
    }



    @Test
    public void test6() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from person_1");
        ResultSetMetaData metaData = resultSet.getMetaData();
        System.out.println(metaData);

    }

    @Test
    public void test7() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("select * from person where id=?");
        statement.setInt(1 , 1);
        ResultSet resultSet = statement.executeQuery();
        printlnResult(resultSet);


    }

    @Test
    public void tset8() throws SQLException {
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();
        int i = statement.executeUpdate("insert into teacher(name) values('hello')");
        System.out.println(i);

        connection.commit();
    }




    @Test
    public void testCon() throws ClassNotFoundException, SQLException, InterruptedException {
        Statement statement = connection.createStatement();
        for(int i = 0 ; i < 100 ; i ++){
            long s = System.currentTimeMillis();
            ResultSet resultSet = statement.executeQuery("select * from coupoaqqqqn;");
            long e = System.currentTimeMillis();
            System.out.println(e - s);
            printlnResult(resultSet);
        }
    }

    @Test
    public void showEngines() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SHOW ENGINES");
        printlnResult(resultSet);
    }





    @Test
    public void test3(){
        System.out.println(ColumnType.VARCHAR);
    }


    private void printlnResult(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()){
            for(int i = 1 ; i <= columnCount;i++){
                Object object = resultSet.getObject(i);
                System.out.print(String.valueOf(object) + "\t");
            }
            System.out.println();
        }
    }

}
