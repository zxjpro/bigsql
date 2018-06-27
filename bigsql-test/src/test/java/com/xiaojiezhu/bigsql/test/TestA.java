package com.xiaojiezhu.bigsql.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.xiaojiezhu.bigsql.sharding.rule.masterslave.DefaultMasterSlaveRule;
import com.xiaojiezhu.bigsql.sql.resolve.SqlResolveUtil;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.util.ScanClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
    public void test3() throws IOException, ClassNotFoundException {
        Set<Class<?>> jarClass = new HashSet<>();
        ScanClassUtil.findJarClass("",new File("E:\\code\\work_space\\css\\css-common\\target\\css-common-0.01.jar"),jarClass);
        System.out.println(jarClass);
    }


}
