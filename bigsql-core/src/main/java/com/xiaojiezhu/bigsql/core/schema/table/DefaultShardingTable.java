package com.xiaojiezhu.bigsql.core.schema.table;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.core.context.BigsqlContext;
import com.xiaojiezhu.bigsql.sharding.Strategy;
import com.xiaojiezhu.bigsql.sharding.rule.Rule;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sql.resolve.statement.Statement;
import com.xiaojiezhu.bigsql.util.BeanUtil;

import java.util.Map;
import java.util.Set;

/**
 * @author xiaojie.zhu
 */
public class DefaultShardingTable extends SimpleTable implements ShardingTable {

    public DefaultShardingTable(String databaseName, String name, BigsqlContext context, Rule rule) {
        super(databaseName,name,context,rule);
    }

    @Override
    public Set<String> listShardingColumn() {
        if(rule instanceof ShardingRule){
            ShardingRule shardingRule = (ShardingRule) rule;
            Map<String, Object> properties = shardingRule.getProperties();
            Object o = properties.get(Rule.SHARDING_COLUMN_NAME_KEY_NAME);
            if(BeanUtil.isStringEmpty(o)){
                throw new BigSqlException(100,"not found " + Rule.SHARDING_COLUMN_NAME_KEY_NAME + " value");
            }else{
                String[] split = o.toString().split(",");
                Set<String> shardingColumn = BeanUtil.toSet(split);
                return shardingColumn;
            }
        }else{
            throw new BigSqlException(300,"rule is not shardingRule , " + rule.getClass());
        }
    }



    @Override
    public Strategy getStrategy(Statement statement) {
        Strategy strategy = createStrategy(statement);
        return strategy;
    }
}
