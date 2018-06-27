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
    private final BigsqlConfiguration configuration;
    private final Map<Channel,String> databaseFlag = new HashMap<>();
    protected String confDirPath;

    private final DataSourcePool dataSourcePool;
    private final Schema schema;

    private final ClassContext classContext;
    private final StrategyPool strategyPool;


    private SimpleBigsqlContext() throws Exception {
        this.configuration = new DefaultBigsqlConfiguration();

        this.confDirPath = this.getBigsqlConfiguration().getConfDirPath();

        this.dataSourcePool = new DefaultDataSourcePool(this.confDirPath);

        this.schema = new DefaultSchema(this);

        File file = new File(configuration.getConfDirPath());

        this.classContext = DefaultClassContext.newInstance(new File(file.getParent() + File.separator + "lib"));

        this.strategyPool = DefaultStrategyPool.newInstance(this.classContext);


    }

    public static SimpleBigsqlContext createInstance() throws Exception {
        SimpleBigsqlContext context = new SimpleBigsqlContext();
        return context;
    }

    @Override
    public String getCurrentDataBase(Channel channel) {
        return databaseFlag.get(channel);
    }

    @Override
    public void setCurrentDataBase(Channel channel, String dataBase) {
        databaseFlag.put(channel,dataBase);
    }

    @Override
    public void removeChannelContext(Channel channel) {
        databaseFlag.remove(channel);
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
