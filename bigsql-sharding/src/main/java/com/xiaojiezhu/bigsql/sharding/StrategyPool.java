package com.xiaojiezhu.bigsql.sharding;

import com.xiaojiezhu.bigsql.common.Reloadable;

/**
 * time 2018/6/25 17:18
 *
 * @author xiaojie.zhu <br>
 */
public interface StrategyPool extends Reloadable {

    /**
     * get strategy by name
     * @param name name
     * @return
     */
    Class<? extends Strategy> getStrategy(String name);

    /**
     * register strategy
     * @param name strategy name
     * @param strategyClass class
     */
    void registerStrategy(String name,Class<? extends Strategy> strategyClass);
}
