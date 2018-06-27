package com.xiaojiezhu.bigsql.server.core.mysql;

/**
 * @author xiaojie.zhu <br>
 * 时间 2018/5/4 20:28
 * 说明 ...
 */
public class NullBitMap {
    private final static int ADD_LENGTH = 2;
    /**
     * it is fixed 7
     */
    private final static int DEFAULT_7 = 7;
    private final static int DEFAULT_8 = 8;

    private byte[] buf;

    public NullBitMap(int columnNumber) {
        int len = calcNullBitMapLength(columnNumber);
        this.buf = new byte[len];
    }


    public void set(int index) {
        int nullBitMapIndex = (index + ADD_LENGTH) / DEFAULT_8;
        int by = (index + ADD_LENGTH) % 8;
        buf[nullBitMapIndex] = (byte) (1 << by);
    }

    public byte[] get(){
        return this.buf;
    }

    private int calcNullBitMapLength(int columnNumber){
        return (columnNumber + ADD_LENGTH + DEFAULT_7) / DEFAULT_8;
    }

}
