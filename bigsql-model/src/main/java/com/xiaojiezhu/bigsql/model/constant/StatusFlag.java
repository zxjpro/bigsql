package com.xiaojiezhu.bigsql.model.constant;

/**
 * https://dev.mysql.com/doc/internals/en/status-flags.html
 * @author xiaojie.zhu
 */
public enum StatusFlag {
    /**
     * 处于活动状态
     */
    SERVER_STATUS_IN_TRANS(0x0001),
    /**
     * 启用自动提交
     */
    SERVER_STATUS_AUTOCOMMIT(0x0002),

    SERVER_MORE_RESULTS_EXISTS(0x0008),

    SERVER_STATUS_NO_GOOD_INDEX_USED(0x0010),

    SERVER_STATUS_NO_INDEX_USED(0x0020),

    /**
     * 用于Binary Protocol Resultset表示 COM_STMT_FETCH 必须用于获取行数据的信号
     */
    SERVER_STATUS_CURSOR_EXISTS(0x0040),

    SERVER_STATUS_LAST_ROW_SENT(0x0080),

    SERVER_STATUS_DB_DROPPED(0x0100),

    SERVER_STATUS_NO_BACKSLASH_ESCAPES(0x0200),

    SERVER_STATUS_METADATA_CHANGED(0x0400),

    SERVER_QUERY_WAS_SLOW(0x0800),

    SERVER_PS_OUT_PARAMS(0x1000),

    /**
     * 在只读事务中
     */
    SERVER_STATUS_IN_TRANS_READONLY(0x2000),

    /**
     * 连接状态信息已更改
     */
    SERVER_SESSION_STATE_CHANGED(0x4000);

    private int value;

    public int getValue(){
        return this.value;
    }

    StatusFlag(int value) {
        this.value = value;
    }


}
