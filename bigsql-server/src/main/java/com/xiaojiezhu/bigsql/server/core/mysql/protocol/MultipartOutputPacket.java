package com.xiaojiezhu.bigsql.server.core.mysql.protocol;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;

import java.util.List;

/**
 * @author xiaojie.zhu<br>
 * 时间 2018/5/4 14:46
 * 说明 a multipart output protocol
 */
public abstract class MultipartOutputPacket extends MySqlOutputPacket{

    public MultipartOutputPacket(int sequenceId) {
        super(sequenceId);
    }

    /**
     * return all packets
     * @return return all of the packet
     */
    public abstract List<MySqlOutputPacket> getAll();

    @Override
    protected void writeBody() {
        List<MySqlOutputPacket> packets = this.getAll();
        if(packets == null || packets.size() == 0){
            throw new BigSqlException("packet size is 0");
        }
        for (MySqlOutputPacket packet : packets) {
            packet.write();
        }
    }
}
