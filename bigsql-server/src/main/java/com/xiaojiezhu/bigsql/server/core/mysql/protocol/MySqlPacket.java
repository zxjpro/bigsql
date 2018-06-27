package com.xiaojiezhu.bigsql.server.core.mysql.protocol;

/**
 * @author xiaojie.zhu
 */
public abstract class MySqlPacket implements Packet{

    public static final int HEADER_LENGTH = 4;

    /**
     * packet byte size,it is not on packet,
     */
    protected int packetLength;
    /**
     * msg sequence id,it use 1 byte
     */
    protected int sequenceId;

    /**
     * packet body byte size,use 3 byte
     */
    protected int bodyLength;






    public int getSequenceId() {
        return sequenceId;
    }
}
