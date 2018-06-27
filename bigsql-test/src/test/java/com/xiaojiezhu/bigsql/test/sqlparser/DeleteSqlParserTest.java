package com.xiaojiezhu.bigsql.test.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

/**
 * @author xiaojie.zhu
 */
public class DeleteSqlParserTest {

    @Test
    public void test(){
        String sql = "DELETE FROM person\n" +
                "WHERE id = '455351507206799360'\n" +
                "\tAND name = '12314'\n" +
                "\tAND age = '12'\n" +
                "\tAND sex = '12'\n" +
                "\tAND tel = '12'\n" +
                "\tAND create_time = '2018-06-10 12:44:16.0'\n" +
                "LIMIT 1";
        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);
        System.out.println(sqlStatement);
    }
}
