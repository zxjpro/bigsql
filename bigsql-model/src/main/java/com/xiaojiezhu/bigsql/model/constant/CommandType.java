package com.xiaojiezhu.bigsql.model.constant;

/**
 * https://dev.mysql.com/doc/internals/en/text-protocol.html
 * @author xiaojie.zhu
 */
public enum  CommandType {

    COM_INIT_DB(0x02),
    COM_QUERY(0x03),
    COM_QUIT(0x01),
    COM_PING(0x0E);

    private int value;

    CommandType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
