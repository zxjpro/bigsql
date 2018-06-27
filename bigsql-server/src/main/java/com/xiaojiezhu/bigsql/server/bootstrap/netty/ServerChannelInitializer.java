package com.xiaojiezhu.bigsql.server.bootstrap.netty;

import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.server.bootstrap.netty.codec.mysql.MysqlProtocolCodec;
import com.xiaojiezhu.bigsql.server.bootstrap.netty.handler.MySqlProtocolHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

/**
 * @author xiaojie.zhu
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final EventLoopGroup bigSqlGroup;
    private final BigsqlContext bigsqlContext;


    public ServerChannelInitializer(EventLoopGroup bigSqlGroup,BigsqlContext context) {
        this.bigSqlGroup = bigSqlGroup;
        this.bigsqlContext = context;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MysqlProtocolCodec());
        pipeline.addLast(new MySqlProtocolHandler(bigSqlGroup,bigsqlContext));
    }
}
