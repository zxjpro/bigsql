package com.xiaojiezhu.bigsql.test.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.xiaojiezhu.bigsql.sharding.SqlUtil;
import com.xiaojiezhu.bigsql.sql.resolve.SqlResolveUtil;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * @author xiaojie.zhu
 */
public class SelectQueryFieldTest {

    @Test
    public void test1(){
        String sql = "select * from user where xx=1";
        SQLSelectStatement sqlStatement = (SQLSelectStatement) SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        List<AliasField> queryField = SqlResolveUtil.getQueryField(sqlStatement);
        Assert.assertTrue(SqlResolveUtil.isQueryAll(queryField));
    }

    @Test
    public void test2(){
        String sql = "select id,name as 'username',age from user where xx=1";
        SQLSelectStatement sqlStatement = (SQLSelectStatement) SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        List<AliasField> queryField = SqlResolveUtil.getQueryField(sqlStatement);
        Assert.assertEquals(3, queryField.size());
    }


    @Test
    public void test3(){
        String sql = "select id,name,age from user";
        String s = SqlUtil.updateTableName(sql, "user", "user_1");
        Assert.assertEquals("select id,name,age from user_1",s);
    }
}
