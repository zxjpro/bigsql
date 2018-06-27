package com.xiaojiezhu.bigsql.test.jdbc;

import com.xiaojiezhu.bigsql.test.ConnectionUtil;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author xiaojie.zhu
 */
public class JdbcTest1 {
    private Connection connection;

    @Before
    public void init() throws SQLException {
        connection = ConnectionUtil.getVir233();
    }

    @Test
    public void insert() throws SQLException {
        String sql = "insert into user(id,name) values(2,'name2')";

        Statement statement = connection.createStatement();
    }
}
