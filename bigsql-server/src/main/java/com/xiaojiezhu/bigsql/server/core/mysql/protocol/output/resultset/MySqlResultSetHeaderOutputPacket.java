package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.resultset;

import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 15:27
 * 说明 resultSet response header
 */
public class MySqlResultSetHeaderOutputPacket extends MySqlOutputPacket {
    private int columnNumber;

    public MySqlResultSetHeaderOutputPacket(int sequenceId,int columnNumber) {
        super(sequenceId);
        this.columnNumber = columnNumber;
    }

    @Override
    protected void writeBody() {
        this.byteBuffer.writeLengthCodeInteger(columnNumber);
    }
}
