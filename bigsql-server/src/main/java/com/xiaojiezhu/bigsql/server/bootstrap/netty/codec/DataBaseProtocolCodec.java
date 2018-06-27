package com.xiaojiezhu.bigsql.server.bootstrap.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
public abstract class DataBaseProtocolCodec<T> extends ByteToMessageCodec<T> {
    public final static Logger LOG = LoggerFactory.getLogger(DataBaseProtocolCodec.class);
    protected abstract void encode0(ChannelHandlerContext ctx, T msg, ByteBuf out);
    protected abstract void decode0(ChannelHandlerContext ctx, ByteBuf in, List<Object> out);


    /**
     * valid the byteBuf is enough
     * @param buf
     * @param readableBytes the buf readable size
     * @return if true  , enough
     */
    protected abstract boolean readPacket(ByteBuf buf, int readableBytes);


    /**
     * in
     * @param ctx
     * @param in
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        LOG.trace("received client data");
        in.markReaderIndex();
        int readableBytes = in.readableBytes();
        if(!readPacket(in,readableBytes)){
            in.resetReaderIndex();
            return;
        }else{
            in.resetReaderIndex();
            ByteBuf copy = in.retainedDuplicate();
            in.skipBytes(in.readableBytes());
            decode0(ctx, copy, out);
        }
    }

    /**
     * out
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception {
        encode0(ctx, msg, out);
    }


}
