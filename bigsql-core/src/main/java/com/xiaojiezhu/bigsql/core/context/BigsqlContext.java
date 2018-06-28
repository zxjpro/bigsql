package com.xiaojiezhu.bigsql.core.context;

import com.xiaojiezhu.bigsql.common.ClassContext;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.schema.Schema;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.StrategyPool;
import io.netty.channel.Channel;

/**
 * @author xiaojie.zhu
 */
public interface BigsqlContext {


    /**
     * get the connectionContext
     * @param channel
     * @return
     */
    ConnectionContext getConnectionContext(Channel channel);

    /**
     * on channel close,should clear the channel data
     * @param channel
     */
    void removeConnectionContext(Channel channel);


    /**
     * get configuration
     * @return configuration
     */
    BigsqlConfiguration getBigsqlConfiguration();

    /**
     * get dataSource pool
     * @return dataSource pool
     */
    DataSourcePool getDataSourcePool();

    /**
     * get the bigsql schema
     * @return schema
     */
    Schema getSchema();

    /**
     * get classContext
     * @return ClassContext
     */
    ClassContext getClassContext();

    /**
     * get strategy pool
     * @return
     */
    StrategyPool getStrategyPool();


    /**
     * reflush
     * @throws Exception
     */
    void refresh()throws Exception;
}
