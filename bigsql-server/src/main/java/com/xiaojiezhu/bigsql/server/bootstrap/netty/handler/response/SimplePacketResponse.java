package com.xiaojiezhu.bigsql.server.bootstrap.netty.handler.response;

import com.xiaojiezhu.bigsql.server.core.mysql.protocol.MultipartOutputPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xiaojie.zhu
 */
public class SimplePacketResponse implements PacketResponse {
    @Override
    public void response(ChannelHandlerContext ctx, MySqlOutputPacket packet) {
        packet.write();
        if(packet instanceof MultipartOutputPacket){
            responseMultipartPacket(ctx, (MultipartOutputPacket) packet);
        }else{
            responsePacket(ctx,packet);
        }
    }

    protected void responsePacket(ChannelHandlerContext ctx,MySqlOutputPacket packet){
        ctx.writeAndFlush(packet);
    }


    protected void responseMultipartPacket(ChannelHandlerContext ctx,MultipartOutputPacket packet){
        for(MySqlOutputPacket p : packet.getAll()){
            ctx.channel().write(p);
        }
        ctx.channel().flush();
    }

    private SimplePacketResponse(){}

    public static SimplePacketResponse getInstance(){
        return Instance.INSTANCE;
    }

    private static class Instance{
        private static final SimplePacketResponse INSTANCE = new SimplePacketResponse();
    }
}
