package com.xiaojiezhu.bigsql.test.sqlparser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.junit.Test;

/**
 * @author xiaojie.zhu
 */
public class UpdateSqlParserTest {

    @Test
    public void test(){
        String sql = "UPDATE `person` SET `name`='98', `tel`='980' WHERE (`id`='455371278430240768') AND (`name`='98') AND (`age`='98') AND (`sex`='98') AND (`tel`='98') AND (`create_time`='2018-06-10 14:02:50.0') LIMIT 1";
        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);

    }
}
