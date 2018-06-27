package com.xiaojiezhu.bigsql.server.core.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author xiaojie.zhu
 */
public class ByteBufferUtil {

    public static ByteBuf getByteBuf(ByteBuffer byteBuffer){
        if(byteBuffer instanceof NettyByteBuffer){
            NettyByteBuffer nettyByteBuffer = (NettyByteBuffer) byteBuffer;
            return nettyByteBuffer.getByteBuf();
        }else{
            byte[] bytes = byteBuffer.toByteArray();
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(bytes);
            return buffer;
        }
    }
}
