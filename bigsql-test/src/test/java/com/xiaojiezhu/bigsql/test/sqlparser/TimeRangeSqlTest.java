package com.xiaojiezhu.bigsql.test.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

/**
 * time 2018/8/10 15:12
 *
 * @author xiaojie.zhu <br>
 */
public class TimeRangeSqlTest {

    @Test
    public void test(){
        String sql = "select * from person where create_time > '2018-01-01' and create_time < '2018-01-09'";
//        String sql = "select * from person where create_time between '2018-01-01' and '2018-01-09'";

        SQLStatement statement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();

        statement.accept(visitor);

        System.out.println(visitor);
    }
}
