package com.xiaojiezhu.bigsql.test.sqlparser;

import com.xiaojiezhu.bigsql.model.constant.CommandType;
import com.xiaojiezhu.bigsql.sql.resolve.statement.InsertStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.StatementHelper;
import com.xiaojiezhu.bigsql.util.increment.IncrementFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class InsertTest {

    /**
     * 把一条insert多行语句转换成为多条语句
     */
    @Test
    public void test(){
        String sql = "insert into user(id,name,age)" +
                "value(1,'abc',23),(11,'1abc',123)";

        InsertStatement statement = (InsertStatement) StatementHelper.parse(CommandType.COM_QUERY, sql);
        statement.addInsertColumn("gen_id",Arrays.asList(IncrementFactory.nextId(),IncrementFactory.nextId()));
        statement.addInsertColumn("hello_id",Arrays.asList(IncrementFactory.nextId(),IncrementFactory.nextId()));
        List<String> insertSql = statement.getInsertSql();
        Assert.assertTrue(insertSql.size() == 2);
    }
}
