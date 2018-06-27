package com.xiaojiezhu.bigsql.server.core.mysql.protocol.input;

import com.xiaojiezhu.bigsql.model.constant.CapabilityFlag;
import com.xiaojiezhu.bigsql.server.core.buffer.ByteBuffer;

/**
 * mysql client auth with username and password
 *
 * jdbc driver: com.mysql.jdbc.Security:scramble411():306
 *
 * @author xiaojie.zhu
 */
public class MySqlAuthInputPacket extends MySqlInputPacket{
    public MySqlAuthInputPacket(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    private int clientAbility;
    private int clientAbilityExtend;
    private int maxMsgLength;
    private int charset;
    private String username;
    private byte[] password;
    private String databaseName;

    @Override
    protected void readBody(ByteBuffer byteBuffer) {
        this.clientAbility = byteBuffer.readShortLE();
        this.clientAbilityExtend = byteBuffer.readShortLE();
        this.maxMsgLength = byteBuffer.readIntLE();
        this.charset = byteBuffer.readByte();
        this.byteBuffer.skipByte(23);
        this.username = byteBuffer.readStringEndZero();
        this.password = byteBuffer.readLengthCodeByte();

        if (0 != (clientAbility & CapabilityFlag.CLIENT_CONNECT_WITH_DB.getValue())) {
            this.databaseName = byteBuffer.readStringEndZero();
        }
    }


    public String getUsername() {
        return username;
    }

    public byte[] getPassword() {
        return password;
    }

    public String getDatabaseName() {
        return databaseName;
    }
}
