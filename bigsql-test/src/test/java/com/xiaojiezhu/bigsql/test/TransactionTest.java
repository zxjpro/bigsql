package com.xiaojiezhu.bigsql.test;

import org.junit.Test;

import java.sql.Connection;
import java.sql.Statement;

/**
 * time 2018/6/27 14:35
 *
 * @author xiaojie.zhu <br>
 */
public class TransactionTest {

    @Test
    public void test() throws Exception {
        Connection connection = ConnectionUtil.getIdc49();
        connection.setAutoCommit(false);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        int i = statement.executeUpdate("insert into teacher(name) values('hello')");
        System.out.println(i);

        connection.commit();
    }
}
