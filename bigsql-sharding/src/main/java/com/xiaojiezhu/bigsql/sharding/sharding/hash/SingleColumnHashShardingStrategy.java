package com.xiaojiezhu.bigsql.sharding.sharding.hash;

import com.xiaojiezhu.bigsql.common.annotation.EnableStrategy;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException;
import com.xiaojiezhu.bigsql.sharding.DataSourcePool;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import com.xiaojiezhu.bigsql.util.BigsqlSystem;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.util.List;

/**
 * sharding by single column
 * @author xiaojie.zhu
 */
@EnableStrategy
public class SingleColumnHashShardingStrategy extends AbstractHashShardingStrategy{


    public SingleColumnHashShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        super(crudStatement, shardingRule);
    }

    @Override
    protected int hashShardingValue(List<? extends ValueField> valueFields) throws ShardingColumnNotExistException {
        if(shardingColumnNames == null || shardingColumnNames.size() == 0){
            throw new ShardingColumnNotExistException("sharding column not exists , table : " + shardingTableName);
        }
        if(shardingColumnNames.size() > 1){
            throw new BigSqlException(100,"sharding column must be single sharding column");
        }

        ValueField valueField = null;
        String shardingColumnName = this.shardingColumnNames.get(0);
        for (ValueField tmp : valueFields) {
            if(shardingColumnName.equals(tmp.getName())){
                valueField = tmp;
                break;
            }
        }

        if(valueField == null){
            throw new BigSqlException(300,"sharding column name [ " + shardingColumnName+ " ] in the sql not found : " + crudStatement.getSql());
        }

        List<Object> values = valueField.getValues();
        if(values == null || values.size() != 1){
            throw new BigSqlException(300,"hash values error , " + values);
        }
        Object o = values.get(0);
        if(TypeUtil.isNumber(o)){
            Long l = Long.parseLong(String.valueOf(o));
            return l.hashCode();
        }else{
            String s = String.valueOf(o).replaceAll("'", "");
            return s.hashCode();
        }
    }
}
