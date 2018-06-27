package com.xiaojiezhu.bigsql.sharding.rule.sharding;

import com.xiaojiezhu.bigsql.sharding.rule.Rule;

import java.util.Map;

/**
 * sharding rule
 * @author xiaojie.zhu
 */
public interface ShardingRule extends Rule {

    /**
     * get the config key-value
     * @return key-value
     */
    Map<String,Object> getProperties();
}
