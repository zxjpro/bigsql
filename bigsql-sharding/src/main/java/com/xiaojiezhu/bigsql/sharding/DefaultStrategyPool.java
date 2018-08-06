package com.xiaojiezhu.bigsql.sharding;

import com.xiaojiezhu.bigsql.common.ClassContext;
import com.xiaojiezhu.bigsql.common.annotation.EnableStrategy;
import com.xiaojiezhu.bigsql.sharding.masterslave.SimpleMasterSlaveStrategy;
import com.xiaojiezhu.bigsql.sharding.sharding.hash.SingleColumnHashShardingStrategy;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * time 2018/6/25 17:20
 *
 * @author xiaojie.zhu <br>
 */
public class DefaultStrategyPool implements StrategyPool {
    public final static Logger LOG = LoggerFactory.getLogger(DefaultStrategyPool.class);
    private ClassContext classContext;
    /**
     * bigsql strategy , <strategyName , class>
     */
    private Map<String,Class<?>> strategyClasses;

    private DefaultStrategyPool(ClassContext classContext) {
        this.classContext = classContext;

        // init bigsql default strategy
        strategyClasses = new HashMap<>(2);

    }

    @Override
    public Class<? extends Strategy> getStrategy(String name) {
        Class<?> strategyClass = strategyClasses.get(name);
        if(strategyClass == null){
            throw new NullPointerException("can not found strategy class : " + name);
        }else{
            return (Class<? extends Strategy>) strategyClass;
        }
    }

    @Override
    public void registerStrategy(String name, Class<? extends Strategy> strategyClass) {
        this.strategyClasses.put(name,strategyClass);
    }

    /**
     * register the strategy , the name is class simple name
     * @param strategyClass class
     */
    private void registerStrategy(Class<? extends Strategy> strategyClass){
        this.registerStrategy(strategyClass.getSimpleName() , strategyClass);
    }


    @Override
    public synchronized void reload() throws Exception {
        LOG.info("reload strategy");
        classContext.reload();


        strategyClasses.clear();
        this.registerDefaultStrategy();
        Set<Class<?>> tmp =  classContext.getByAnnotation(EnableStrategy.class);
        if(!BeanUtil.isEmpty(tmp)){
            for (Class<?> strategyClass : tmp) {
                this.registerStrategy((Class<? extends Strategy>) strategyClass);
            }
        }
        LOG.info("reload strategy success , strategy :" + strategyClasses.keySet());
    }


    /**
     * register bigsql default strategy
     */
    private void registerDefaultStrategy(){
        this.registerStrategy(SimpleMasterSlaveStrategy.class);
        this.registerStrategy(SingleColumnHashShardingStrategy.class);
    }


    public static DefaultStrategyPool newInstance(ClassContext classContext) throws Exception {
        DefaultStrategyPool pool = new DefaultStrategyPool(classContext);
        pool.reload();
        return pool;
    }
}
