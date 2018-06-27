package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output;

import com.xiaojiezhu.bigsql.server.core.buffer.ByteBuffer;
import com.xiaojiezhu.bigsql.server.core.buffer.NettyByteBuffer;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.MySqlPacket;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.PacketWriter;
import com.xiaojiezhu.bigsql.util.IOUtil;

/**
 * @author xiaojie.zhu
 */
public abstract class MySqlOutputPacket extends MySqlPacket implements PacketWriter {
    protected ByteBuffer byteBuffer;
    /**
     * is invoke write method
     */
    private boolean writed;

    public MySqlOutputPacket(int sequenceId) {
        this.sequenceId = sequenceId;
    }


    @Override
    public int packetSize() {
        if(byteBuffer == null){
            return -1;
        }
        return byteBuffer.length();
    }

    @Override
    public void write() {
        synchronized (this){
            if(!writed){
                this.writed = true;

                this.byteBuffer = new NettyByteBuffer();
                this.writeBody();
                this.bodyLength = this.byteBuffer.length();
                ByteBuffer packet = new NettyByteBuffer();
                packet.writeMediumLE(this.bodyLength);
                packet.writeByte(this.sequenceId);
                packet.append(this.byteBuffer);
                IOUtil.close(this.byteBuffer);
                this.byteBuffer = packet;
                this.packetLength = HEADER_LENGTH + this.bodyLength;
            }else{
                throw new BigSqlException("can not invoke write(T obj) method twice");
            }
        }
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    /**
     * write body
     */
    protected abstract void writeBody();
}
