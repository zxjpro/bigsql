package com.xiaojiezhu.bigsql.sql.resolve.statement;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.model.constant.CommandType;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public class StatementHelper {
    public final static Logger LOG = LoggerFactory.getLogger(StatementHelper.class);

    public static Statement parse(CommandType commandType,String sql)throws SqlParserException {
        sql = sql.replaceAll("`","");
        if(CommandType.COM_INIT_DB.equals(commandType)){
            return new CommandStatement(sql);
        }else{
            return parseComQuery(sql);
        }
    }

    private static Statement parseComQuery(String sql)throws SqlParserException{
        List<SQLStatement> sqlStatements = null;
        try {
            sqlStatements = SQLUtils.parseStatements(sql, Constant.SQL_TYPE);
        } catch (Exception e) {
            throw new SqlParserException("sql parser error :" + sql );
        }
        if(sqlStatements == null || sqlStatements.size() == 0){
            throw new SqlParserException("sql resolve error , " + sql);
        }else if(sqlStatements.size() > 1){
            throw new SqlParserException("bigsql just resolve single sql , there has " + sqlStatements.size() + " , " +sql);
        }else{
            SQLStatement sqlStatement = sqlStatements.get(0);
            sql = sqlStatement.toString().replaceAll("/\\*[\\s\\S]*\\*/","").trim();
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            if(sql.startsWith(SqlConstant.SELECT)){
                if(!sql.contains(SqlConstant.FROM)){
                    //simple select,such select now(),select @@xxx
                    Statement statement = SimpleSelectStatement.parse(sql);
                    return statement;
                }else{
                    //TODO select from table

                    sqlStatement.accept(visitor);
                    DefaultCommandSelectStatement selectStatement = new DefaultCommandSelectStatement(sql,sqlStatement,visitor);
                    selectStatement.setResponseNull(isResponseNull(visitor));
                    return selectStatement;

                }
            }else if(sql.startsWith(SqlConstant.INSERT)){
                //INSERT
                return new DefaultInsertStatement(sql, (SQLInsertStatement) sqlStatement,visitor);
            }else if(sql.startsWith(SqlConstant.DELETE)){
                //DELETE
                return new DeleteStatement(sql,sqlStatement, visitor);
            }else if(sql.startsWith(SqlConstant.UPDATE)){
                //UPDATE
                return new UpdateStatement(sql,sqlStatement, visitor);
            }else if(sql.startsWith(SqlConstant.SET)){
                //such as 'SET NAMES utf8mb4'
                return new SettingStatement(sql);
            }else if(sql.startsWith(SqlConstant.SHOW)){
                return parseShowQuery(sql);
            }
        }

        throw new SqlParserException("not support sql : " + sql);
    }

    private static Statement parseShowQuery(String sql) {
        if(sql.startsWith(SqlConstant.SHOW_DATABASES)){
            return new ShowComponentStatement(sql);
        }else if(sql.startsWith(SqlConstant.SHOW_TABLES)){
            return new ShowComponentStatement(sql);
        }else if(sql.startsWith(SqlConstant.SHOW_VARIABLES)){
            //such as 'SHOW VARIABLES LIKE 'lower_case_%''
            return new VariableStatement(sql);
        }else if(sql.startsWith(SqlConstant.SHOW_STATUS)){
            //SHOW STATUS
            return new VariableStatement(sql);
        }else if(sql.startsWith(SqlConstant.SHOW_CREATE_TABLE)){
            //show create table
            return new ShowCreateTableStatement(sql);
        }else if(sql.startsWith(SqlConstant.SHOW_ENGINES)){
            //SHOW ENGINES
            // try no handler
            return new NoHandlerStatement(sql);
        }else{
            LOG.warn("no handler sql : " + sql);
            return new NoHandlerStatement(sql);
        }
    }


    public static boolean isResponseNull(MySqlSchemaStatVisitor visitor){
        Map<TableStat.Name, TableStat> tables = visitor.getTables();
        Set<TableStat.Name> names = tables.keySet();
        for (TableStat.Name name : names) {
            if(name.toString().contains(".")){
                return true;
            }
        }
        return false;

    }



}
