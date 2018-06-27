package com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.resultset;

import com.xiaojiezhu.bigsql.model.constant.ColumnFlag;
import com.xiaojiezhu.bigsql.model.constant.ColumnType;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.server.core.mysql.protocol.output.MySqlOutputPacket;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 15:45
 * 说明 response column type
 */
public class MySqlColumnDefinitionOutputPacket extends MySqlOutputPacket {
    /**
     * the column display size * BASE_NUMBER
     */
    public static final int BASE_NUMBER = 3;

    private final String catalog = "def";

    private String databaseName;

    /**
     * AS之后的名称
     */
    private String tableName;

    /**
     * AS之前的名称
     */
    private String realTableName;

    /**
     * AS之后的名称
     */
    private String fieldName;

    /**
     * AS之前的名称
     */
    private String realFieldName;

    /**
     * 这是一个固定值
     */
    private final int fixedField = 0x0c;

    /**
     * charset ,use 2 byte LE
     */
    private int charset = Constant.CHARSET;

    /**
     * 字段长度，类似varchar(50)，这里的长度是50,use 4 byte le
     */
    private int fieldLength;

    /**
     * use 1 byte
     */
    private ColumnType columnType;

    /**
     *  use 2 byte
     */
    private ColumnFlag columnFlag;

    /**
     * use 1 byte
     * 0x00 整数和静态字符串
     * 0x1f 对于动态字符串，double，float
     * 0x00到 0x51小数点
     *
     */
    private int precision;

    private final byte filler = 0x00;

    public MySqlColumnDefinitionOutputPacket(int sequenceId) {
        super(sequenceId);
    }

    @Override
    protected void writeBody() {
        this.byteBuffer.writeLengthCodeString(catalog);
        this.byteBuffer.writeLengthCodeString(databaseName);
        this.byteBuffer.writeLengthCodeString(tableName);
        this.byteBuffer.writeLengthCodeString(realTableName);
        this.byteBuffer.writeLengthCodeString(fieldName);
        this.byteBuffer.writeLengthCodeString(realFieldName);
        this.byteBuffer.writeLengthCodeInteger(fixedField);
        this.byteBuffer.writeShortLE(charset);
        this.byteBuffer.writeIntLE(fieldLength * 3);
        this.byteBuffer.writeByte(columnType.getValue());
        this.byteBuffer.writeShortLE(columnFlag.getValue());
        this.byteBuffer.writeByte(precision);
        this.byteBuffer.writeShortLE(filler);
    }


    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setRealTableName(String realTableName) {
        this.realTableName = realTableName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setRealFieldName(String realFieldName) {
        this.realFieldName = realFieldName;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public void setColumnFlag(ColumnFlag columnFlag) {
        this.columnFlag = columnFlag;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }
}
