package com.xiaojiezhu.bigsql.server.core.mysql.protocol.input;

import com.xiaojiezhu.bigsql.server.core.buffer.ByteBuffer;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.MySqlPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.PacketReader;
import com.xiaojiezhu.bigsql.util.Asserts;

/**
 * input mysql packet
 * @author xiaojie.zhu
 */
public abstract class MySqlInputPacket extends MySqlPacket implements PacketReader {

    protected ByteBuffer byteBuffer;
    private boolean read;

    public MySqlInputPacket(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.read(byteBuffer);
    }


    @Override
    public void read(ByteBuffer byteBuffer) {
        Asserts.notNull(byteBuffer,"buffer can not be null");
        synchronized (byteBuffer){
            if(!read){
                this.read = true;
                this.bodyLength = byteBuffer.readMediumLE();
                this.sequenceId = byteBuffer.readByte();
                this.packetLength = HEADER_LENGTH + this.bodyLength;
                this.readBody(byteBuffer);
            }else{
                throw new BigSqlException("can not invoke read(ByteBuffer byteBuffer) method twice");
            }
        }
    }

    /**
     * read protocol body
     * @param byteBuffer
     */
    protected abstract void readBody(ByteBuffer byteBuffer);

    @Override
    public int packetSize() {
        return byteBuffer.length();
    }


}
