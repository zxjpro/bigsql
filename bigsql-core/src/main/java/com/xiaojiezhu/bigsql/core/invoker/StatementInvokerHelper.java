package com.xiaojiezhu.bigsql.core.invoker;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.context.ConnectionContext;
import com.xiaojiezhu.bigsql.model.constant.CommandType;
import com.xiaojiezhu.bigsql.sql.resolve.statement.*;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

/**
 * create statementInvoker by commandType and statement
 * @author xiaojie.zhu
 */
public class StatementInvokerHelper {
    private BigsqlContext context;
    private final EventLoopGroup bigsqlGroup;


    public StatementInvokerHelper(BigsqlContext context,EventLoopGroup bigsqlGroup) {
        this.context = context;
        this.bigsqlGroup = bigsqlGroup;
    }

    public StatementInvoker getStatementInvoker(CommandType commandType, Statement statement, Channel channel){
        if(statement == null){
            throw new NullPointerException("statement can not be null");
        }

        if(CommandType.COM_QUERY.equals(commandType)){
            ConnectionContext connectionContext = context.getConnectionContext(channel);
            if(statement instanceof SimpleSelectStatement){
                return new EnvironmentStatementInvoker(statement,context.getBigsqlConfiguration(),connectionContext);
            }else if(statement instanceof SettingStatement){
                return new SettingStatementInvoker(statement);
            }else if(statement instanceof DefaultCommandSelectStatement){
                //select
                String currentDataBase = connectionContext.getCurrentDataBase();
                return new CrudStatementInvoker(statement,context,connectionContext, bigsqlGroup,currentDataBase);
            }else if(statement instanceof InsertStatement){
                //insert
                String currentDataBase = connectionContext.getCurrentDataBase();
                return new CrudStatementInvoker(statement,context,connectionContext,bigsqlGroup,currentDataBase);
            }else if(statement instanceof DeleteStatement){
                //delete
                String currentDataBase = connectionContext.getCurrentDataBase();
                return new CrudStatementInvoker(statement,context,connectionContext,bigsqlGroup,currentDataBase);
            }else if(statement instanceof UpdateStatement){
                String currentDataBase = connectionContext.getCurrentDataBase();
                return new CrudStatementInvoker(statement,context,connectionContext,bigsqlGroup,currentDataBase);
            }else if(statement instanceof VariableStatement){
                return new VariableStatementInvoker(statement,context.getBigsqlConfiguration());
            }else if(statement instanceof ShowComponentStatement){
                ShowComponentStatement showComponentStatement = (ShowComponentStatement) statement;
                if(ShowComponentStatement.Type.DATABASES.equals(showComponentStatement.getShowType())){
                    //show schema
                    return new ShowDataBaseInvoker(statement , context);
                }else if(ShowComponentStatement.Type.TABLES.equals(showComponentStatement.getShowType())){
                    //show table
                    return new ShowTableInvoker(statement,connectionContext.getCurrentDataBase(),context);
                }else{
                    throw new BigSqlException("error showComponentStatement : " + statement);
                }

            }else if(statement instanceof ShowCreateTableStatement){
                return new ShowCreateTableInvoker(statement);
            }else if(statement instanceof NoHandlerStatement){
                return new NullResultInvoker(statement);
            }else if(statement instanceof ShowEngineStatement){
                return new ShowComponentInvoker(statement,context);
            }else if(statement instanceof TransactionStatement){
                //transaction
                return new TransactionInvoker(statement,connectionContext);
            }
        }else if(CommandType.COM_INIT_DB.equals(commandType)){
            return new InitDataBaseStatementInvoker(statement,context,channel);
        }

        throw new RuntimeException("not support statement : " + statement.getSql());

    }
}
