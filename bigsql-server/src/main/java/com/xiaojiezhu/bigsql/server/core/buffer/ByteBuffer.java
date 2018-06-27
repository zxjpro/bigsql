package com.xiaojiezhu.bigsql.server.core.buffer;


import java.io.Closeable;

/**
 * provide byte method
 * @author xiaojie.zhu
 */
public interface ByteBuffer extends Closeable{

    /**
     * get length
     * @return
     */
    int length();

    /**
     * read string from index, end of 0x00
     * @return
     */
    String readStringEndZero();

    /**
     * read a byte,and readIndex + 1
     * @return
     */
    byte readByte();


    void writeByte(int i);
    void writeBytes(byte[] bu);

    void writeByte(byte b);

    /**
     * write a int ,use 4 byte
     * @param intLe
     */
    void writeIntLE(int intLe);

    /**
     * write a int,use 4 byte
     * @param value
     */
    void writeInt(int value);

    /**
     * write length 0x00 byte
     * @param len
     */
    void writeNull(int len);

    /**
     * write a short ,use 2 byte
     * @param shortLE
     */
    void writeShortLE(int shortLE);

    /**
     * write int ,use 3 byte
     * @param mediumLE
     */
    void writeMediumLE(int mediumLE);

    /**
     * to byte array
     * @return
     */
    byte[] toByteArray();

    /**
     * append t byteBuffer
     * @param byteBuffer
     */
    void append(ByteBuffer byteBuffer);

    /**
     * read a int , use 3 byte
     * @return
     */
    int readMediumLE();

    /**
     * read a int,use 2 byte
     * @return
     */
    int readShortLE();

    /**
     * read a int ,use 4 byte
     * @return
     */
    int readIntLE();

    /**
     * write a string,and end of 0x00
     */
    void writeStringWithZero(String str);

    /**
     * read a length code binary,this is mysql protocol data format
     * @return
     */
    byte[] readLengthCodeByte();

    byte[] readBytes(int len);

    /**
     * skip to reader index
     * @param len
     */
    void skipByte(int len);

    /**
     * write length code binary,
     * https://dev.mysql.com/doc/internals/en/integer.html#length-encoded-integer
     * @param value
     */
    void writeLengthCodeInteger(long value);

    /**
     * write a long value,use 8 bit
     * @param value
     */
    void writeLongLE(long value);

    /**
     * write a string
     * @param msg
     */
    void writeString(String msg);

    /**
     * read the bytes to the buffer end index
     * @return
     */
    byte[] readBytesEnd();


    /**
     * write length code string
     * https://dev.mysql.com/doc/internals/en/string.html#packet-Protocol::VariableLengthString
     * @param value
     */
    void writeLengthCodeString(String value);
}
