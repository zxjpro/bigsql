package com.xiaojiezhu.bigsql.test.tx;

import com.xiaojiezhu.bigsql.test.ConnectionUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * time 2018/8/9 17:32
 *
 * @author xiaojie.zhu <br>
 */
public class TransactionTest {

    @Test
    public void test() throws Exception {
        Connection connection = ConnectionUtil.getLocalhostSharding();
        connection.setAutoCommit(false);
        try {
            for(int i = 0 ; i < 10 ; i ++){
                PreparedStatement preparedStatement = connection.prepareStatement("insert into person(name,sex,tel,age,create_time) values(?,?,?,?,now())");
                preparedStatement.setString(1,"name" + i);
                preparedStatement.setInt(2,0);
                preparedStatement.setString(3,"tel" + i);
                preparedStatement.setInt(4,i);

                int r = preparedStatement.executeUpdate();
                System.out.println(r);

                if(i == 9){
                    int a = 1 / 0;
                }

            }

            connection.commit();
            System.out.println("over");

        } catch (Exception e) {
            System.err.println("rollback");
            connection.rollback();
        }


    }


}
