package com.xiaojiezhu.bigsql.test.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.DefaultCommandSelectStatement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * time 2018/8/13 10:06
 *
 * @author xiaojie.zhu <br>
 */
public class RangeSqlTest {

    @Test
    public void test() {
        String sql = "select * from person where create_time > '2018-01-01' and create_time < '2019-01-01'";

        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();

        DefaultCommandSelectStatement statement = new DefaultCommandSelectStatement(sql,sqlStatement,visitor);

        List<ConditionField> conditionFields = statement.getConditionFields();

        Assert.assertEquals("[create_time=[> [2018-01-01], < [2019-01-01]]]" , conditionFields.toString());
    }


    @Test
    public void testBetweenAnd(){
        String sql = "select * from person where create_time between '2018-01-01' and   '2019-01-01'";
        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();

        DefaultCommandSelectStatement statement = new DefaultCommandSelectStatement(sql,sqlStatement,visitor);

        List<ConditionField> conditionFields = statement.getConditionFields();

        Assert.assertEquals("[create_time=[>= 2018-01-01, <= 2019-01-01]]" , conditionFields.toString());
    }
}
