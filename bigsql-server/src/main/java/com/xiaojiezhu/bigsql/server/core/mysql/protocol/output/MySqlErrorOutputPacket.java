package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.util.ByteUtil;

/**
 * error packet
 * time 2018/5/22 17:10
 *
 * https://dev.mysql.com/doc/internals/en/packet-ERR_Packet.html
 * @author xiaojie.zhu <br>
 */
public class MySqlErrorOutputPacket extends MySqlOutputPacket {
    private static final int SERVER_STATUS_LENGTH = 5;
    /**
     * 1 byte
     */
    private final int flag = 0xFF;

    /**
     * 2 byte,LE
     */
    private int errorCode;

    /**
     * 1 byte
     */
    private final String serverFlag = "#";

    /**
     * 5 byte
     */
    private byte[] serverStatus;

    /**
     *
     */
    private String errorMsg;



    public MySqlErrorOutputPacket(int sequenceId,int errorCode,String serverStatus,String errorMsg) {
        super(sequenceId);
        byte[] bytes = serverStatus.getBytes();
        if(bytes.length < SERVER_STATUS_LENGTH){
            int l = 5 - bytes.length;
            byte[] buf = new byte[l];
            //can not response 0 byte, because jdbc mysql driver not show error info
            for(int i = 0 ; i < l ; i ++){
                buf[i] = 95;
            }
            bytes = ByteUtil.concat(bytes,buf);
        }
        if(bytes.length > SERVER_STATUS_LENGTH){
            throw new BigSqlException("server status must be 5 byte");
        }
        this.serverStatus = bytes;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    protected void writeBody() {
        this.byteBuffer.writeByte(flag);
        this.byteBuffer.writeShortLE(errorCode);
        this.byteBuffer.writeBytes(serverFlag.getBytes());
        this.byteBuffer.writeBytes(serverStatus);
        this.byteBuffer.writeString(errorMsg);
    }
}
