package com.xiaojiezhu.bigsql.server.bootstrap.netty.handler;

import com.xiaojiezhu.bigsql.core.context.ConnectionContext;

/**
 * time 2018/8/9 21:02
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultClientLeaveHandler implements ClientLeaveHandler {

    private DefaultClientLeaveHandler() {
    }

    @Override
    public void onLeave(ConnectionContext connectionContext) {

        try {
            connectionContext.destroy();
        } catch (Throwable e) {
            //nothing to do
        }
    }



    public static ClientLeaveHandler getInstance(){
        return INSTANCE;
    }

    private final static DefaultClientLeaveHandler INSTANCE = new DefaultClientLeaveHandler();
}
