package com.xiaojiezhu.bigsql.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlShowColumnOutpuVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/9 11:55
 * 说明 ...
 */
public class SqlParseParser {

    //String sql = "select id,name as 'username',age as ages,nation,grade,create_time,tel from user u where id=12 order by create_time desc";
    String sql = "SELECT  @@session.auto_increment_increment AS auto_increment_increment, @@character_set_client AS character_set_client, @@character_set_connection AS character_set_connection, @@character_set_results AS character_set_results, @@character_set_server AS character_set_server, @@collation_server AS collation_server, @@init_connect AS init_connect, @@interactive_timeout AS interactive_timeout, @@license AS license, @@lower_case_table_names AS lower_case_table_names, @@max_allowed_packet AS max_allowed_packet, @@net_buffer_length AS net_buffer_length, @@net_write_timeout AS net_write_timeout, @@query_cache_size AS query_cache_size, @@query_cache_type AS query_cache_type, @@sql_mode AS sql_mode, @@system_time_zone AS system_time_zone, @@time_zone AS time_zone, @@tx_isolation AS transaction_isolation, @@wait_timeout AS wait_timeout";
    @Test
    public void test()  {

        SQLStatementParser sqlStatementParser = new SQLStatementParser(sql, JdbcConstants.MYSQL);

        SQLStatement sqlStatement = sqlStatementParser.parseStatement();


        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        Collection<TableStat.Column> columns = visitor.getColumns();

        System.out.println(columns);

        List<TableStat.Condition> conditions = visitor.getConditions();
        System.out.println(conditions);

        List<TableStat.Column> orderByColumns = visitor.getOrderByColumns();
        System.out.println(orderByColumns);

        List<Object> parameters = visitor.getParameters();
        System.out.println(parameters);

        Map<TableStat.Name, TableStat> tables = visitor.getTables();
        System.out.println(tables);

    }

    @Test
    public void testParser(){
        String s = SQLUtils.addCondition("select * from user where name='abc'", "id=2", JdbcConstants.MYSQL);
        System.out.println(s);
    }

    @Test
    public void test4(){
        String sql = "select * from user where name='xx' and id=231";

        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);
        List<TableStat.Condition> conditions = visitor.getConditions();

        System.out.println(conditions.get(0).getValues());
    }

    @Test
    public void test5(){
        String sql = "select * from user where name in('a','b','c')";
        sql = "delete from user where name in('a','b','c')";
        SQLStatement sqlStatement = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        List<TableStat.Condition> conditions = visitor.getConditions();

        System.out.println(conditions);
    }

    @Test
    public void test6(){
        String sql = "insert into user(id,name,age)values(21,'a1',121)";

        SQLInsertStatement sqlStatement = (SQLInsertStatement) SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
        SQLName tableName = sqlStatement.getTableName();
        System.out.println(tableName.getSimpleName());

        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        sqlStatement.accept(visitor);

        List<Object> parameters = visitor.getParameters();
        System.out.println(parameters);

        SQLInsertStatement.ValuesClause values = sqlStatement.getValues();
        List<SQLExpr> sqlExprs = values.getValues();
        SQLExpr sqlExpr = sqlExprs.get(0);
        System.out.println(sqlExpr);


        StringBuilder sb = new StringBuilder();
        MySqlOutputVisitor visitor1 = new MySqlOutputVisitor(sb);

        sqlStatement.accept(visitor1);

    }

    @Test
    public void test7(){
        String sql = "insert into user(id,name,age)values(21,'a1',121)";
        MySqlInsertStatement sqlStatement = (MySqlInsertStatement) SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);

        StringBuilder sb = new StringBuilder();
        MySqlOutputVisitor visitor = new MySqlOutputVisitor(sb);
        sqlStatement.accept(visitor);

        SQLInsertStatement.ValuesClause valuesClause = new SQLInsertStatement.ValuesClause();
        SQLInsertStatement.ValuesClause values = sqlStatement.getValues();
        values.addValue(new SQLIntegerExpr(999));

        List<SQLExpr> columns = sqlStatement.getColumns();
        columns.add(new SQLIdentifierExpr("genId"));

        System.out.println(sqlStatement);


    }


}
