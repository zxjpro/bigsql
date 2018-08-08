package com.xiaojiezhu.bigsql.test.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SimpleField;
import com.xiaojiezhu.bigsql.sql.resolve.field.SortField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.DefaultCommandSelectStatement;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * time 2018/8/8 11:47
 *
 * @author xiaojie.zhu <br>
 */
public class SelectParseTest2 {

    @Test
    public void findGroupBy(){
        String sql = "SELECT count(1),sex FROM person where age >1  group by sex;";

        SQLStatement statement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);


        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        DefaultCommandSelectStatement s = new DefaultCommandSelectStatement(sql,statement,visitor);


        SimpleField groupField = s.getGroupField();

        Assert.assertEquals("sex",groupField.getName());

        List<AliasField> queryField = s.getQueryField();
        System.out.println(queryField);
    }

    @Test
    public void findOrderBy(){
        String sql = "select name,age from person order by age";

        SQLStatement statement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);


        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        DefaultCommandSelectStatement s = new DefaultCommandSelectStatement(sql,statement,visitor);

        SortField orderField = s.getOrderField();

        Assert.assertEquals(orderField.getName(),"age");
        Assert.assertEquals(orderField.isAsc(),true);
    }

    @Test
    public void findOrderBy2(){
        String sql = "select name,age from person order by age desc";

        SQLStatement statement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);


        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        DefaultCommandSelectStatement s = new DefaultCommandSelectStatement(sql,statement,visitor);

        SortField orderField = s.getOrderField();

        Assert.assertEquals(orderField.getName(),"age");
        Assert.assertEquals(orderField.isAsc(),false);
    }
}
