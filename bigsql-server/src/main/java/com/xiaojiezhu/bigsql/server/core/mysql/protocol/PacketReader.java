package com.xiaojiezhu.bigsql.server.core.mysql.protocol;

import com.xiaojiezhu.bigsql.server.core.buffer.ByteBuffer;

/**
 * packet reader
 * @author xiaojie.zhu
 */
public interface PacketReader {

    /**
     * read byte array to object
     * @param byteBuffer
     */
    void read(ByteBuffer byteBuffer);
}
