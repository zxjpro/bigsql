package com.xiaojiezhu.bigsql.server.core.buffer;


import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.ProtocolErrorException;
import com.xiaojiezhu.bigsql.model.constant.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

/**
 * implement by netty
 * @author xiaojie.zhu
 */
public class NettyByteBuffer implements ByteBuffer{
    private ByteBuf buf;

    public NettyByteBuffer(ByteBuf buf) {
        this.buf = buf;
    }

    public NettyByteBuffer() {
        this.buf = Unpooled.buffer();
    }

    @Override
    public int length() {
        return buf.readableBytes();
    }

    @Override
    public String readStringEndZero() {
        int len = this.buf.bytesBefore((byte) 0);
        byte[] tmp = new byte[len];
        this.buf.readBytes(tmp);
        this.skipByte(1);
        return new String(tmp);
    }

    @Override
    public byte readByte() {
        return this.buf.readByte();
    }

    @Override
    public void writeByte(int i) {
        this.buf.writeByte(i);
    }

    @Override
    public void writeBytes(byte[] bu) {
        this.buf.writeBytes(bu);
    }

    @Override
    public void writeByte(byte b) {
        this.buf.writeByte(b);
    }

    @Override
    public void writeIntLE(int intLe) {
        this.buf.writeIntLE(intLe);
    }

    @Override
    public void writeInt(int value) {
        this.buf.writeInt(value);
    }

    @Override
    public void writeNull(int len) {
        for(int i = 0 ; i < len ; i ++){
            this.buf.writeByte(Constant.NULL);
        }
    }

    @Override
    public void writeShortLE(int shortLE) {
        buf.writeShortLE(shortLE);
    }

    @Override
    public void writeMediumLE(int mediumLE) {
        buf.writeMediumLE(mediumLE);
    }

    @Override
    public byte[] toByteArray() {
        synchronized (this){
            if(buf == null){
                return null;
            }else{
                int len = buf.readableBytes();
                byte[] tmp = new byte[len];
                this.buf.markReaderIndex().readBytes(tmp);
                this.buf.resetReaderIndex();
                return tmp;
            }
        }
    }

    @Override
    public void append(ByteBuffer byteBuffer) {
        buf.writeBytes(byteBuffer.toByteArray());
    }

    @Override
    public int readMediumLE() {
        return buf.readMediumLE();
    }

    @Override
    public int readShortLE() {
        return buf.readShortLE();
    }

    @Override
    public int readIntLE() {
        return buf.readIntLE();
    }

    @Override
    public void writeStringWithZero(String str) {
        this.buf.writeBytes(str.getBytes());
        this.writeNull(1);
    }

    @Override
    public byte[] readLengthCodeByte() {
        byte tag = this.buf.readByte();
        if(tag <= Constant._250){
            return this.readBytes(tag);
        }else if(tag == Constant._251){
            return null;
        }else if(tag == Constant._252){
            return this.readBytes(this.buf.readShortLE());
        }else if(tag == Constant._253){
            return this.readBytes(this.buf.readMediumLE());
        }else if(tag == Constant._254){
            return this.readBytes((int) this.buf.readLongLE());
        }else{
            throw new ProtocolErrorException("mysql auth protocol : error length code binary first byte:" + tag);
        }
    }
    @Override
    public void writeLengthCodeInteger(long value) {
        if(value <= Constant._250){
            this.writeByte((int)value);
            return;
        }else if(value >= Constant._251 && value < Math.pow(Constant._2,Constant._16)){
            this.writeByte(0xfc);
            this.writeShortLE((int)value);
            return;
        }else if(value >= Math.pow(Constant._2,Constant._16) && value < Math.pow(Constant._2,Constant._24)){
            this.writeByte(0xfc);
            this.writeMediumLE((int)value);
        }else if(value >= Math.pow(Constant._2,Constant._24) && value < Math.pow(Constant._2,Constant._64)){
            this.writeByte(0xfe);
            this.writeLongLE(value);
        }else{
            throw new BigSqlException("error length code binary :" + value);
        }
    }

    @Override
    public void writeLongLE(long value) {
        this.buf.writeLongLE(value);
    }

    @Override
    public void writeString(String msg) {
        this.buf.writeBytes(msg.getBytes());
    }

    @Override
    public byte[] readBytesEnd() {
        int len = this.buf.readableBytes();
        byte[] b = new byte[len];
        this.buf.readBytes(b);
        return b;
    }

    @Override
    public void writeLengthCodeString(String value_) {
        if(value_ == null || value_.length() == 0){
            this.writeNull(1);
        }else{
            int len = value_.getBytes().length;
            this.writeLengthCodeInteger(len);
            this.writeString(value_);
        }

    }

    @Override
    public byte[] readBytes(int len) {
        byte[] tmp = new byte[len];
        this.buf.readBytes(tmp);
        return tmp;
    }

    @Override
    public void skipByte(int len) {
        this.buf.skipBytes(len);
    }



    public ByteBuf getByteBuf(){
        return this.buf;
    }


    @Override
    public void close() throws IOException {
        if(buf != null){
            buf.release();
        }
    }
}
