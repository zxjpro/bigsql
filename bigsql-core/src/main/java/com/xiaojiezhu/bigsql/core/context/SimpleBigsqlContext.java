package com.xiaojiezhu.bigsql.core.context;

import com.xiaojiezhu.bigsql.common.ClassContext;
import com.xiaojiezhu.bigsql.common.DefaultClassContext;
import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.configuration.DefaultBigsqlConfiguration;
import com.xiaojiezhu.bigsql.core.configuration.datasource.DefaultDataSourcePool;
import com.xiaojiezhu.bigsql.core.schema.DefaultSchema;
import com.xiaojiezhu.bigsql.core.schema.Schema;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.DefaultStrategyPool;
import com.xiaojiezhu.bigsql.sharding.StrategyPool;
import io.netty.channel.Channel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * call createInstance to create instance
 * @author xiaojie.zhu
 */
public class SimpleBigsqlContext implements BigsqlContext {
    private final Map<Channel,ConnectionContext> connectionContextMap;
    private final BigsqlConfiguration configuration;
    protected String confDirPath;

    private final DataSourcePool dataSourcePool;
    private final Schema schema;

    private final ClassContext classContext;
    private final StrategyPool strategyPool;


    private SimpleBigsqlContext(String password) throws Exception {
        this.connectionContextMap = new HashMap<>();

        this.configuration = new DefaultBigsqlConfiguration(password);

        this.confDirPath = this.getBigsqlConfiguration().getConfDirPath();

        this.dataSourcePool = new DefaultDataSourcePool(this.confDirPath);

        this.schema = new DefaultSchema(this);

        File file = new File(configuration.getConfDirPath());

        this.classContext = DefaultClassContext.newInstance(new File(file.getParent() + File.separator + "lib"));

        this.strategyPool = DefaultStrategyPool.newInstance(this.classContext);


    }

    public static SimpleBigsqlContext createInstance(String password) throws Exception {
        SimpleBigsqlContext context = new SimpleBigsqlContext(password);
        return context;
    }


    @Override
    public ConnectionContext getConnectionContext(Channel channel) {
        ConnectionContext connectionContext = connectionContextMap.get(channel);
        if(connectionContext == null){
            connectionContext = new SimpleConnectionContext(dataSourcePool);
            connectionContextMap.put(channel,connectionContext);
        }
        return connectionContext;
    }

    @Override
    public void removeConnectionContext(Channel channel) {
        connectionContextMap.remove(channel);
    }

    @Override
    public BigsqlConfiguration getBigsqlConfiguration() {
        return configuration;
    }

    @Override
    public DataSourcePool getDataSourcePool() {
        return dataSourcePool;
    }

    @Override
    public Schema getSchema() {
        return schema;
    }

    @Override
    public ClassContext getClassContext() {
        return classContext;
    }

    @Override
    public StrategyPool getStrategyPool() {
        return strategyPool;
    }


    @Override
    public void refresh()throws Exception {
        this.configuration.reload();
        this.dataSourcePool.reload();
        this.schema.reload();
        this.classContext.reload();
        this.strategyPool.reload();
    }
}
