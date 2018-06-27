package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output;

/**
 * OK packet
 * @author xiaojie.zhu
 */
public class MySqlOkOutputPacket extends MySqlOutputPacket {

    /**
     * default 0x00 is ok
     */
    private final int flag = 0x00;

    private long affectRow;

    /**
     * // TODO : last insert id
     * 该值为AUTO_INCREMENT索引字段生成，如果没有索引字段，则为0x00。注意：当INSERT插入语句为多行数据时，该索引ID值为第一个插入的数据行索引值，而非最后一个。
     */
    private long insertId;

    /**
     * //TODO : status
     * 客户端可以通过该值检查命令是否在事务处理中
     */
    private int serverStatus;
    /**
     * warning number
     */
    private int warnNumber;

    /**
     * response msg,it can be null
     */
    private String responseMsg = "";




    public MySqlOkOutputPacket(int sequenceId) {
        super(sequenceId);
    }

    @Override
    protected void writeBody() {
        this.byteBuffer.writeByte(this.flag);
        this.byteBuffer.writeLengthCodeInteger(this.affectRow);
        this.byteBuffer.writeLengthCodeInteger(this.insertId);
        this.byteBuffer.writeShortLE(this.serverStatus);
        this.byteBuffer.writeShortLE(this.warnNumber);
        this.byteBuffer.writeString(this.responseMsg);
    }


    public void setAffectRow(long affectRow) {
        this.affectRow = affectRow;
    }

    public void setInsertId(long insertId) {
        this.insertId = insertId;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public void setWarnNumber(int warnNumber) {
        this.warnNumber = warnNumber;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
