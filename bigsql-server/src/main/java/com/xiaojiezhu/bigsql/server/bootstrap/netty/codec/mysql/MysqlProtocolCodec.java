package com.xiaojiezhu.bigsql.server.bootstrap.netty.codec.mysql;

import com.xiaojiezhu.bigsql.server.bootstrap.netty.codec.DataBaseProtocolCodec;
import com.xiaojiezhu.bigsql.server.core.buffer.ByteBuffer;
import com.xiaojiezhu.bigsql.server.core.buffer.ByteBufferUtil;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.MySqlPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.PacketWriter;
import com.xiaojiezhu.bigsql.util.IOUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class MysqlProtocolCodec extends DataBaseProtocolCodec<PacketWriter> {


    @Override
    protected boolean readPacket(ByteBuf buf, int readableBytes) {
        int len = buf.readMediumLE();
        return readableBytes >= MySqlPacket.HEADER_LENGTH + len;
    }

    @Override
    protected void decode0(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(in);
    }


    @Override
    protected void encode0(ChannelHandlerContext ctx, PacketWriter msg, ByteBuf out) {
        ByteBuffer byteBuffer = msg.getByteBuffer();
        ByteBuf buffer = ByteBufferUtil.getByteBuf(byteBuffer);
        out.writeBytes(buffer);
        IOUtil.close(byteBuffer);
    }




}
