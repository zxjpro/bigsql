package com.xiaojiezhu.bigsql.server.bootstrap.netty.handler;

import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicLong;

/**
 * init context
 * @author xiaojie.zhu
 */
public abstract class AbstractProtocolHandler<I> extends SimpleChannelInboundHandler<I> {
    public final static Logger LOG = LoggerFactory.getLogger(AbstractProtocolHandler.class);
    protected final EventLoopGroup bigSqlGroup;
    protected final BigsqlContext bigsqlContext;
    /**
     * count channel
     */
    private static final AtomicLong CHANNEL_COUNT = new AtomicLong();

    public AbstractProtocolHandler(EventLoopGroup bigSqlGroup, BigsqlContext bigsqlContext) {
        this.bigsqlContext = bigsqlContext;
        this.bigSqlGroup = bigSqlGroup;
    }

    /**
     * increment channel number
     * @param ctx
     * @throws Exception
     */
    @Override
    public final void channelActive(ChannelHandlerContext ctx) throws Exception {
        long channelNumber = CHANNEL_COUNT.incrementAndGet();
        LOG.info("coming a client : [" + getRemoteAddress(ctx.channel()) + "] , connection number : " + channelNumber);
        this.channelActive0(ctx);
    }


    @Override
    public final void channelInactive(ChannelHandlerContext ctx) throws Exception {
        long channelNumber = CHANNEL_COUNT.decrementAndGet();
        bigsqlContext.removeConnectionContext(ctx.channel());
        LOG.info("a client leave : [" + getRemoteAddress(ctx.channel()) + "] , connection number : " + channelNumber);
        this.channelInactive0(ctx);

    }

    /**
     * to channel active
     * @param ctx
     */
    protected abstract void channelActive0(ChannelHandlerContext ctx)throws Exception;

    /**
     * to channel inActive
     * @param ctx
     * @throws Exception
     */
    protected abstract void channelInactive0(ChannelHandlerContext ctx)throws Exception;

    protected String getRemoteAddress(Channel channel){
        InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
        return inetSocketAddress.getAddress().getHostAddress();
    }
}
