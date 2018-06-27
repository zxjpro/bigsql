package com.xiaojiezhu.bigsql.server.core.mysql.protocol;

import com.xiaojiezhu.bigsql.model.constant.StatusFlag;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;

/**
 * @author xiaojie.zhu<br>
 * 时间 2018/5/3 16:41
 * 说明 ...
 */
public class MySqlEofOutputPacket extends MySqlOutputPacket{
    private final int flag = 0xFE;

    private int warnNumber;

    private StatusFlag statusFlag;

    public MySqlEofOutputPacket(int sequenceId, int warnNumber, StatusFlag statusFlag) {
        super(sequenceId);
        this.warnNumber = warnNumber;
        this.statusFlag = statusFlag;
    }

    public int getFlag() {
        return flag;
    }

    public int getWarnNumber() {
        return warnNumber;
    }

    public StatusFlag getStatusFlag() {
        return statusFlag;
    }

    @Override
    protected void writeBody() {
        this.byteBuffer.writeByte(flag);
        this.byteBuffer.writeShortLE(warnNumber);
        this.byteBuffer.writeShortLE(statusFlag.getValue());
    }
}
