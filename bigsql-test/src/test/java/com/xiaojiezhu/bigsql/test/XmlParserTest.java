package com.xiaojiezhu.bigsql.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.DefaultShardingRule;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * time 2018/5/24 11:46
 *
 * @author xiaojie.zhu <br>
 */
public class XmlParserTest {
    @Test
    public void test() throws Exception {
        String file = "G:\\javacode\\bigsql\\template\\tableName.xml";
        DefaultShardingRule defaultShardingRule = DefaultShardingRule.create(new File(file));
        System.out.println(defaultShardingRule);
    }

}
