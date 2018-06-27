package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.resultset;

import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.util.List;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 18:59
 * 说明 ...
 */
public class MySqlRowDataOutputPacket extends MySqlOutputPacket {


    private int columnNumber;
    private List<Object> rowData;

    public MySqlRowDataOutputPacket(int sequenceId, int columnNumber, List<Object> rowData) {
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
        for (Object obj : rowData) {
            if(obj == null){
                this.byteBuffer.writeByte(Constant.ROW_NULL);
            }else{
                if(TypeUtil.isNumber(obj)){
                    this.byteBuffer.writeLengthCodeInteger(Long.parseLong(String.valueOf(obj)));
                }else{
                    this.byteBuffer.writeLengthCodeString(String.valueOf(obj));
                }
            }

        }
    }





}
