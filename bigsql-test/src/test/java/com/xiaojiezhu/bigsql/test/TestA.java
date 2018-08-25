package com.xiaojiezhu.bigsql.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.DruidDataSourceUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.xiaojiezhu.bigsql.sharding.rule.masterslave.DefaultMasterSlaveRule;
import com.xiaojiezhu.bigsql.sharding.sharding.time.standard.StandardRange;
import com.xiaojiezhu.bigsql.sql.resolve.SqlResolveUtil;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.util.DateUtils;
import com.xiaojiezhu.bigsql.util.TypeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class TestA {

    @Test
    public void test1(){
        String sql = "select * from user where id=1 and name='2' and ab in (1,2,3)";
        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        List<ConditionField> condition = SqlResolveUtil.getCondition(sqlStatement);
        Assert.assertEquals(3, condition.size());
    }

    @Test
    public void test2() throws Exception {
        DefaultMasterSlaveRule rule = DefaultMasterSlaveRule.create(new File("E:\\code\\work_space\\bigsql\\template\\default.xml"));
        System.out.println(rule);


    }




    @Test
    public void test3(){
        String s = "[20170101-20180101]=dataSource1";


        StandardRange range = new StandardRange(s,"YEAR");

        String dataSourceName = range.getDataSourceName();

        Assert.assertEquals("dataSource1",dataSourceName);

        Assert.assertTrue(range.isRange(DateUtils.parse("2017-01-01","yyyy-MM-dd")));

        Assert.assertFalse(range.isRange(DateUtils.parse("2018-01-01","yyyy-MM-dd")));

        Assert.assertTrue(range.isRange(DateUtils.parse("2017-12-31 23:59:59","yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void test4() throws ParseException {
        Assert.assertTrue("2017-11-11".matches("([\\d]{4}?)-([\\d]+?)-([\\d]+?)"));
        Assert.assertTrue("2018-01-01".matches("([\\d]{4}?)-([\\d]+?)-([\\d]+?)"));
        Assert.assertTrue("2018-2-01".matches("([\\d]{4}?)-([\\d]+?)-([\\d]+?)"));
        Assert.assertTrue("2018-2-9".matches("([\\d]{4}?)-([\\d]+?)-([\\d]+?)"));
        Assert.assertTrue("2018-02-9".matches("([\\d]{4}?)-([\\d]+?)-([\\d]+?)"));
        Assert.assertTrue("2018-02-09 10:22:11".matches("([\\d]{4}?)-([\\d]+?)-([\\d]+?) ([\\d]+?):([\\d]+?):([\\d]+)"));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = format.parse("2018-3-9 9:8:12");

        System.out.println(parse.toLocaleString());
    }

    @Test
    public void test5(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH , 0);
        calendar.set(Calendar.DAY_OF_MONTH , 1);
        calendar.set(Calendar.HOUR_OF_DAY , 0);
        calendar.set(Calendar.MINUTE , 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND  ,0);


        System.out.println(calendar.getTime().toLocaleString());
    }

    @Test
    public void test6(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH , 11);
        calendar.set(Calendar.HOUR_OF_DAY , 23);
        calendar.set(Calendar.MINUTE , 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND  ,999);

        int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH , maxDayOfMonth);


        System.out.println(calendar.getTime().toLocaleString());
    }


    @Test
    public void test7() throws ParseException {
        Assert.assertFalse(TypeUtil.isInteger("123.06"));
        Assert.assertTrue(TypeUtil.isInteger("123"));
        Assert.assertFalse(TypeUtil.isDouble("123"));
        Assert.assertTrue(TypeUtil.isDouble("123.06"));

        Date date = TypeUtil.parseDate("2018-08-20 10:53:11.964");

    }

    @Test
    public void testDs() throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:mysql://192.168.31.233:3306/saas?useUnicode=true&characterEncoding=utf8&useSSL=false");
        ds.setUsername("root");
        ds.setPassword("123456");
        ds.setMaxActive(34);


        DruidPooledConnection connection = ds.getConnection();
        DruidPooledConnection connection1 = ds.getConnection();
        DruidPooledConnection connection2 = ds.getConnection();
        DruidPooledConnection connection3 = ds.getConnection();

        System.out.println("当前激活的数量" + ds.getActiveCount());
        System.out.println(ds.getActivePeak());
        System.out.println();

        System.out.println("connectionCount:" + ds.getConnectCount());
        System.out.println("closeCount:" + ds.getCloseCount());

        System.out.println("创建连接的数量:" + ds.getCreateCount());

        connection.close();

        System.out.println("当前激活的数量" + ds.getActiveCount());
        System.out.println(ds.getActivePeak());
        System.out.println();

        DruidPooledConnection connection4 = ds.getConnection();
        System.out.println("当前激活的数量" + ds.getActiveCount());
        System.out.println("激活的峰值" + ds.getActivePeak());
        System.out.println();

        System.out.println("允许的最大激活数量：" + ds.getMaxActive());

        System.out.println(ds.getPoolingCount());
        System.out.println(ds.getPoolingPeak());
        System.out.println();


        System.out.println("connectionCount:" + ds.getConnectCount());
        System.out.println("closeCount:" + ds.getCloseCount());

        System.out.println("创建连接的数量:" + ds.getCreateCount());

        int poolingCount = ds.getPoolingCount();
        System.out.println(poolingCount);



    }

}
