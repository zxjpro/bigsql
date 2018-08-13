package com.xiaojiezhu.bigsql.test;

import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.model.constant.CommandType;
import com.xiaojiezhu.bigsql.sql.resolve.statement.SimpleSelectStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.StatementHelper;
import com.xiaojiezhu.bigsql.sql.resolve.field.AliasField;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class StatementHelperTest {

    @Test
    public void test1() throws SqlParserException {
        String sql = "SELECT  @@session.auto_increment_increment AS auto_increment_increment, @@character_set_client AS character_set_client, @@character_set_connection AS character_set_connection, @@character_set_results AS character_set_results, @@character_set_server AS character_set_server, @@collation_server AS collation_server, @@init_connect AS init_connect, @@interactive_timeout AS interactive_timeout, @@license AS license, @@lower_case_table_names AS lower_case_table_names, @@max_allowed_packet AS max_allowed_packet, @@net_buffer_length AS net_buffer_length, @@net_write_timeout AS net_write_timeout, @@query_cache_size AS query_cache_size, @@query_cache_type AS query_cache_type, @@sql_mode AS sql_mode, @@system_time_zone AS system_time_zone, @@time_zone AS time_zone, @@tx_isolation AS transaction_isolation, @@wait_timeout AS wait_timeout";

        SimpleSelectStatement statement = (SimpleSelectStatement) StatementHelper.parse(CommandType.COM_QUERY,sql);

        List<AliasField> queryField = statement.getQueryField();
        System.out.println(queryField);

        Assert.assertTrue(queryField.size() > 0);
    }

}
