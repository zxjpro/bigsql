package com.xiaojiezhu.bigsql.sharding.sharding.hash;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sql.resolve.field.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;

import java.util.List;

/**
 * not support
 * multipart sharding column hash strategy
 * @author xiaojie.zhu
 */
public class MultipartColumnHashShardingStrategy extends AbstractHashShardingStrategy {
    public MultipartColumnHashShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        super(crudStatement, shardingRule);
    }

    @Override
    protected int hashShardingValue(List<? extends Field> fields) throws ShardingColumnNotExistException {
        throw new BigSqlException("not complete code");
    }
}
