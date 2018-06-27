package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.resultset;

import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;

/**
 * @author xiaojie.zhu
 */
public class MySqlTextRowDataOutputPacket extends MySqlOutputPacket {
    private static final int NULL = 0xfb;
    private Object[] objects;


    public MySqlTextRowDataOutputPacket(int sequenceId,Object[] objects) {
        super(sequenceId);
        this.objects = objects;
    }

    @Override
    protected void writeBody() {
        for (Object object : objects) {
            if(object == null){
                this.byteBuffer.writeByte(NULL);
            }else{
                this.byteBuffer.writeLengthCodeString(String.valueOf(object));
            }
        }
    }
}
