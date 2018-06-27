package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output;

import com.xiaojiezhu.bigsql.model.constant.CapabilityFlag;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.model.constant.StatusFlag;
import com.xiaojiezhu.bigsql.server.core.mysql.AuthRandom;

/**
 * @author xiaojie.zhu
 */
public class MySqlHandshakeOutputPacket extends MySqlOutputPacket{

    private int protocolVersion = Constant.MYSQL_PROTOCOL_VERSION;
    private String serverInfo = Constant.SERVER_INFO;
    private int connectionId;
    private AuthRandom authRandom;
    private int capabilityflags = CapabilityFlag.calculateHandshakeCapabilityFlagsLower();
    private int charset = Constant.CHARSET;
    private StatusFlag statusFlag = StatusFlag.SERVER_STATUS_AUTOCOMMIT;
    private int capabilityflagsUpper = CapabilityFlag.calculateHandshakeCapabilityFlagsUpper();


    public MySqlHandshakeOutputPacket(int sequenceId,int connectionId,AuthRandom authRandom) {
        super(sequenceId);
        this.connectionId = connectionId;
        this.authRandom = authRandom;
    }

    @Override
    protected void writeBody() {
        this.byteBuffer.writeByte(this.protocolVersion);
        this.byteBuffer.writeStringWithZero(this.serverInfo);
        this.byteBuffer.writeIntLE(this.connectionId);
        this.byteBuffer.writeBytes(this.authRandom.getR1());
        this.byteBuffer.writeNull(1);
        this.byteBuffer.writeShortLE(capabilityflags);
        this.byteBuffer.writeByte(charset);
        this.byteBuffer.writeShortLE(statusFlag.getValue());
        this.byteBuffer.writeShortLE(capabilityflagsUpper);

        this.byteBuffer.writeByte(Constant.NULL);
        this.byteBuffer.writeNull(10);
        this.byteBuffer.writeBytes(this.authRandom.getR2());
        this.byteBuffer.writeByte(Constant.NULL);

    }


    public AuthRandom getAuthRandom() {
        return authRandom;
    }
}
