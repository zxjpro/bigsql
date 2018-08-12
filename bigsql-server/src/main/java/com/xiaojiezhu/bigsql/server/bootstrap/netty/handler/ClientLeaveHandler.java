package com.xiaojiezhu.bigsql.server.bootstrap.netty.handler;

import com.xiaojiezhu.bigsql.core.context.ConnectionContext;

/**
 * time 2018/8/9 21:00
 * client leave
 * @author xiaojie.zhu <br>
 */
public interface ClientLeaveHandler {

    /**
     * on client leave
     * @param connectionContext
     */
    void onLeave(ConnectionContext connectionContext);

}
