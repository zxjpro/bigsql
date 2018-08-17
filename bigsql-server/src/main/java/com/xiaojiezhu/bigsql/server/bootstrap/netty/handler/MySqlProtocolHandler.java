package com.xiaojiezhu.bigsql.server.bootstrap.netty.handler;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.SqlParserException;
import com.xiaojiezhu.bigsql.core.auth.AuthenticationService;
import com.xiaojiezhu.bigsql.core.auth.DefaultAuthenticationService;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.invoker.InitDataBaseStatementInvoker;
import com.xiaojiezhu.bigsql.core.invoker.StatementInvoker;
import com.xiaojiezhu.bigsql.core.invoker.StatementInvokerHelper;
import com.xiaojiezhu.bigsql.core.invoker.result.ExecuteInvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.InvokeResult;
import com.xiaojiezhu.bigsql.core.invoker.result.SelectInvokeResult;
import com.xiaojiezhu.bigsql.model.constant.CommandType;
import com.xiaojiezhu.bigsql.model.constant.StatusFlag;
import com.xiaojiezhu.bigsql.server.bootstrap.netty.handler.response.PacketResponse;
import com.xiaojiezhu.bigsql.server.bootstrap.netty.handler.response.SimplePacketResponse;
import com.xiaojiezhu.bigsql.server.core.buffer.NettyByteBuffer;
import com.xiaojiezhu.bigsql.server.core.mysql.AuthRandom;
import com.xiaojiezhu.bigsql.server.core.mysql.ConnectionIdCreator;
import com.xiaojiezhu.bigsql.server.core.mysql.SimpleConnectionIdCreator;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.input.MySqlAuthInputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.input.MySqlCommandInputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlErrorOutputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlHandshakeOutputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOkOutputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.resultset.MySqlResultSetOutputPacket;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CommandStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.StatementHelper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xiaojie.zhu
 */
public class MySqlProtocolHandler extends AbstractProtocolHandler<ByteBuf> {
    public final static Logger LOG = LoggerFactory.getLogger(MySqlProtocolHandler.class);




    private ConnectionIdCreator connectionIdCreator = SimpleConnectionIdCreator.getInstance();
    private AuthRandom authRandom = new AuthRandom();
    private AuthenticationService authenticationService = new DefaultAuthenticationService(bigsqlContext.getBigsqlConfiguration());
    private boolean auth;
    private PacketResponse packetResponse = SimplePacketResponse.getInstance();

    private StatementInvokerHelper statementInvokerHelper;



    public MySqlProtocolHandler(EventLoopGroup bigSqlGroup, BigsqlContext bigsqlContext) {
        super(bigSqlGroup, bigsqlContext);
        statementInvokerHelper = new StatementInvokerHelper(bigsqlContext,bigSqlGroup);
    }


    @Override
    protected void channelActive0(ChannelHandlerContext ctx) throws Exception {
        //connection
        handshake(ctx);
    }

    @Override
    protected void channelInactive0(ChannelHandlerContext ctx) throws Exception {
        //leave
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();

    }

    private void handshake(ChannelHandlerContext ctx) {
        MySqlHandshakeOutputPacket packet = new MySqlHandshakeOutputPacket(0,connectionIdCreator.get(),authRandom);
        packetResponse.response(ctx,packet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        if(!auth){
            auth(ctx, msg);
        }else{
            MySqlCommandInputPacket commandPacket = null;
            try {
                commandPacket = new MySqlCommandInputPacket(new NettyByteBuffer(msg));
            } catch (Exception e) {
                LOG.error("resolve protocol fail , " + e.getMessage() , e);
                packetResponse.response(ctx , new MySqlErrorOutputPacket(1,400 , "" , "resolve protocol fail : " + e.getMessage()));
            }

            String sql = commandPacket.getSql();
            if(sql != null){
                LOG.info(sql);
                //TODO: 需要改成异步的方式
                Statement statement = null;
                try {
                    statement = StatementHelper.parse(commandPacket.getCommandType(),commandPacket.getSql());
                } catch (SqlParserException e) {
                    LOG.error("sql parser error : " + e.getMessage());
                    packetResponse.response(ctx , new MySqlErrorOutputPacket(1,e.getCode(),"",e.getMessage()));
                    return;
                }catch (Throwable e){
                    LOG.error("sql parser error : " + e.getMessage());
                    packetResponse.response(ctx , new MySqlErrorOutputPacket(1, 500 ,"" , e.getMessage()));
                    return;
                }

                InvokeResult invokeResult = execute(ctx, commandPacket.getCommandType(), statement);
                if(invokeResult == null){
                    return;
                }
                responseCommand(ctx, invokeResult);
            }else{
                if(CommandType.COM_QUIT.equals(commandPacket.getCommandType())){
                    LOG.info("客户端退出连接 [" + getRemoteAddress(ctx.channel()) + "]");
                }else if(CommandType.COM_PING.equals(commandPacket.getCommandType())){
                    // ping
                    packetResponse.response(ctx,new MySqlOkOutputPacket(1));
                }else{
                    LOG.warn("收到一个为空的SQL，这是代码有问题");
                }
            }
        }
    }

    /**
     * execute command
     * @param ctx
     * @param commandType
     * @param statement
     * @return
     */
    private InvokeResult execute(ChannelHandlerContext ctx,CommandType commandType,Statement statement){
        StatementInvoker statementInvoker = statementInvokerHelper.getStatementInvoker(commandType,statement,ctx.channel());
        InvokeResult invokeResult;
        try {
            invokeResult = statementInvoker.invoke();
            return invokeResult;
        } catch (BigSqlException e) {
            LOG.error("invoke statement error : "+ e.getMessage() , e);
            MySqlErrorOutputPacket errPacket = new MySqlErrorOutputPacket(1,e.getCode(),"",e.getMessage());
            packetResponse.response(ctx,errPacket);
            return null;
        } catch (Exception e){
            LOG.error("invoke statement error : "+ e.getMessage() , e);
            MySqlErrorOutputPacket errPacket = new MySqlErrorOutputPacket(1,300,"",e.getMessage());
            packetResponse.response(ctx,errPacket);
            return null;
        }
    }

    /**
     * response to client
     * @param ctx
     * @param invokeResult result data
     * @throws SQLException
     */
    private void responseCommand(ChannelHandlerContext ctx, InvokeResult invokeResult) throws SQLException {
        if(invokeResult instanceof SelectInvokeResult){
            SelectInvokeResult selectInvokeResult = (SelectInvokeResult) invokeResult;
            MySqlResultSetOutputPacket resultSetPacket = null;
            try {
                resultSetPacket = makeResultPacket(selectInvokeResult);
            } catch (Exception e) {
                LOG.error("generate result package fail ",e);
                packetResponse.response(ctx,new MySqlErrorOutputPacket(1,400 ,"","generate result packet fail : " + e.getMessage()));
                return;
            }
            packetResponse.response(ctx,resultSetPacket);
        }else if(invokeResult instanceof ExecuteInvokeResult){
            ExecuteInvokeResult executeInvokeResult = (ExecuteInvokeResult) invokeResult;
            if(executeInvokeResult.isSuccess()){
                MySqlOkOutputPacket okOutputPacket = new MySqlOkOutputPacket(1);
                okOutputPacket.setAffectRow(executeInvokeResult.getAffectRow());
                packetResponse.response(ctx,okOutputPacket);
            }else{
                MySqlErrorOutputPacket errPacket = new MySqlErrorOutputPacket(1,300,"",executeInvokeResult.getErrorMsg());
                packetResponse.response(ctx,errPacket);
            }
        }
    }

    private void auth(ChannelHandlerContext ctx, ByteBuf msg) {
        MySqlAuthInputPacket packet = new MySqlAuthInputPacket(new NettyByteBuffer(msg));
        if(packet.getDatabaseName() != null){
            InitDataBaseStatementInvoker initDataBaseStatementInvoker = new InitDataBaseStatementInvoker(new CommandStatement(packet.getDatabaseName()),bigsqlContext,ctx.channel() );
            initDataBaseStatementInvoker.invoke();
        }
        boolean login = authenticationService.login(packet.getUsername(), packet.getPassword(), authRandom.getR3());
        if(login){
            this.auth = true;
            LOG.debug("user:" + packet.getUsername() + " login success");
            MySqlOkOutputPacket okPacket = new MySqlOkOutputPacket(packet.getSequenceId() + 1);
            okPacket.setServerStatus(StatusFlag.SERVER_STATUS_AUTOCOMMIT.getValue());
            packetResponse.response(ctx,okPacket);
        }else{
            LOG.debug("user:" + packet.getUsername() + " login fail");
            packetResponse.response(ctx , new MySqlErrorOutputPacket(1 , 403 ,"","login fail"));
        }
    }


    protected MySqlResultSetOutputPacket makeResultPacket(SelectInvokeResult selectInvokeResult) throws SQLException {
        ResultSet resultSet = selectInvokeResult.getResultSet();
        if(resultSet == null){
        }
        return MySqlResultSetOutputPacket.create(resultSet);
    }




}
