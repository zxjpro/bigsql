package com.xiaojiezhu.bigsql.server.core.mysql.protocol;

import com.xiaojiezhu.bigsql.server.core.buffer.ByteBuffer;

/**
 * packet writer
 *
 * @author xiaojie.zhu
 */
public interface PacketWriter {

    /**
     * write to byte array
     */
    void write();

    /**
     * when write byte,you can get a byteBuffer
     * @return
     */
    ByteBuffer getByteBuffer();
}
