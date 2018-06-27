package com.xiaojiezhu.bigsql.server.bootstrap.netty;

import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.core.context.SimpleBigsqlContext;
import com.xiaojiezhu.bigsql.server.bootstrap.Bootstrap;
import com.xiaojiezhu.bigsql.server.config.BigSqlConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author xiaojie.zhu
 */
public class DefaultBootstrap implements Bootstrap {
    public final static Logger LOG = LoggerFactory.getLogger(DefaultBootstrap.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private EventLoopGroup bigSqlGroup;
    private ChannelFuture channelFuture;
    private BigsqlContext context;

    public DefaultBootstrap() {
        try {
            context = SimpleBigsqlContext.createInstance();
        } catch (Exception e) {
            LOG.info("create bigsql context fail" , e);
            System.exit(0);
        }
    }

    @Override
    public void startServer(final BigSqlConfig bigSqlConfig) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = createBossGroup(bigSqlConfig.getBossThread());
        if(bossGroup instanceof EpollEventLoopGroup){
            bindEpoll(bootstrap, bigSqlConfig.getWorkerThread(),bigSqlConfig.getBigSqlThread());
        }else{
            bindNio(bootstrap, bigSqlConfig.getWorkerThread(),bigSqlConfig.getBigSqlThread());
        }
        try {
            channelFuture = bootstrap.bind(bigSqlConfig.getHost(), bigSqlConfig.getPort()).sync();
            LOG.info("bigsql start success , " + bigSqlConfig.getHost() + ":" + bigSqlConfig.getPort());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            bigSqlGroup.shutdownGracefully();
        }

    }



    private void bindNio(final ServerBootstrap bootstrap,int workerThread,int bigSqlThread){
        workerGroup = new NioEventLoopGroup(workerThread);
        bigSqlGroup = new NioEventLoopGroup(bigSqlThread);
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        //bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 300);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        bootstrap.childHandler(new ServerChannelInitializer(bigSqlGroup,context));
    }

    private void bindEpoll(final ServerBootstrap bootstrap,int workerThread,int bigSqlThread){
        workerGroup = new EpollEventLoopGroup(workerThread);
        bigSqlGroup = new EpollEventLoopGroup(bigSqlThread);
        bootstrap.group(bossGroup,workerGroup);
        bootstrap.channel(EpollServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 128);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(EpollChannelOption.TCP_CORK, true);
        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.handler(new LoggingHandler(LogLevel.DEBUG));
        bootstrap.childHandler(new ServerChannelInitializer(bigSqlGroup,context));
    }

    private EventLoopGroup createBossGroup(int threadNum) {
        EventLoopGroup group;
        try {
            //window do not support
            group = new EpollEventLoopGroup(threadNum);
        } catch (Throwable e) {
            LOG.debug("can not create EpollEventLoopGroup,it will create NioEventLoopGroup");
            group = new NioEventLoopGroup(threadNum);
        }
        LOG.debug("create boss group success, " + threadNum + " thread");
        return group;
    }

    @Override
    public void close() throws IOException {
        try {
            channelFuture.channel().close().sync();
            LOG.info("bigSql is close success!");
        } catch (InterruptedException e) {
            String errMsg = "bigSql close fail!";
            LOG.error(errMsg,e);
            throw new IOException(errMsg,e);
        }
    }



}
