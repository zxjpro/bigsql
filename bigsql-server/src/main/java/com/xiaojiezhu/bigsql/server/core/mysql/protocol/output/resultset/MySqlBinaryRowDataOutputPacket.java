package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.resultset;

import com.xiaojiezhu.bigsql.server.core.mysql.NullBitMap;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.util.List;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 20:01
 * 说明 ...
 */
public class MySqlBinaryRowDataOutputPacket extends MySqlOutputPacket{
    /**
     * default flag,use 1 byte
     */
    private final int flag = 0x00;

    private List<Object> rowData;

    private int columnNumber;

    public MySqlBinaryRowDataOutputPacket(int sequenceId,int columnNumber,List<Object> rowData) {
        super(sequenceId);
        if(rowData == null || rowData.size() == 0){
            throw new BigSqlException("row data can not be empty");
        }
        if(rowData.size() != columnNumber){
            throw new BigSqlException("column number must equals row data size");
        }
        this.columnNumber = columnNumber;
        this.rowData = rowData;

    }

    @Override
    protected void writeBody() {
        this.byteBuffer.writeByte(flag);
        NullBitMap nullBitMap = new NullBitMap(this.columnNumber);
        for(int i = 0 ; i < columnNumber ; i ++){
            if(rowData.get(i) == null){
                nullBitMap.set(i);
            }
        }

        for(byte b : nullBitMap.get()){
            this.byteBuffer.writeByte(b);
        }

        for(int i = 0 ; i < rowData.size() ; i ++){
            Object data = rowData.get(i);
            if(data == null){
                continue;
            }else{
                if(TypeUtil.isNumber(data)){
                    this.byteBuffer.writeLengthCodeInteger(Long.parseLong(String.valueOf(data)));
                }else{
                    this.byteBuffer.writeLengthCodeString(String.valueOf(data));
                }
            }
        }

    }
}
