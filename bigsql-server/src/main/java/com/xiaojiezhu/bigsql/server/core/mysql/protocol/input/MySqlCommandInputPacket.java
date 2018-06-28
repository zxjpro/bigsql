package com.xiaojiezhu.bigsql.server.core.mysql.protocol.input;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.model.constant.CommandType;
import com.xiaojiezhu.bigsql.server.core.buffer.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaojie.zhu<br>
 * 时间 2018/5/2 16:36
 * 说明 ...
 */
public class MySqlCommandInputPacket extends MySqlInputPacket {
    public final static Logger LOG = LoggerFactory.getLogger(MySqlCommandInputPacket.class);
    protected String sql;
    protected CommandType commandType;


    public MySqlCommandInputPacket(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }

    @Override
    protected void readBody(ByteBuffer byteBuffer) {
        //TODO : 这里有多种客户端请求方式，不止有com_query，需要重构
        //TODO: 需要改成异步返回
        byte b = byteBuffer.readByte();
        if(b == CommandType.COM_QUERY.getValue()){
            this.commandType = CommandType.COM_QUERY;
            byte[] cmdBytes = byteBuffer.readBytesEnd();
            this.sql = new String(cmdBytes);
        }else if(b == CommandType.COM_INIT_DB.getValue()){
            this.commandType = CommandType.COM_INIT_DB;
            byte[] cmdBytes = byteBuffer.readBytesEnd();
            this.sql = new String(cmdBytes);
        }else if(b == CommandType.COM_QUIT.getValue()){
            this.commandType = CommandType.COM_QUIT;
        }else if(b == CommandType.COM_PING.getValue()){
            this.commandType = CommandType.COM_PING;
        }else{
            //TODO : 未完成的协议
            throw new BigSqlException("not support sql command , flag: " + b);
        }

    }


    public CommandType getCommandType() {
        return commandType;
    }

    public String getSql() {
        return this.sql;
    }
}
