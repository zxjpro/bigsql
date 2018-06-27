package com.xiaojiezhu.bigsql.sharding.rule;

/**
 * sharding rule
 * @author xiaojie.zhu
 */
public interface Rule {
    final String STRATEGY_TAG_NAME = "strategy";
    final String PROPERTIES_TAG_NAME = "properties";



    final String SHARDING_COLUMN_NAME_KEY_NAME = "shardingColumn";

    /**
     * return the rule use strategy name
     * @return strategy name
     */
    String getStrategyName();
}
