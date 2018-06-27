package com.xiaojiezhu.bigsql.server.bootstrap.netty.handler.response;

import com.xiaojiezhu.bigsql.server.core.mysql.protocol.Packet;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xiaojie.zhu
 */
public interface PacketResponse {

    void response(ChannelHandlerContext ctx,MySqlOutputPacket packet);
}
