package com.xiaojiezhu.bigsql.sharding.sharding.hash;

import com.xiaojiezhu.bigsql.common.annotation.EnableStrategy;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException;
import com.xiaojiezhu.bigsql.sharding.SqlUtil;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.field.Expression;
import com.xiaojiezhu.bigsql.sql.resolve.field.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
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
    protected int hashShardingValue(List<? extends Field> fields) throws ShardingColumnNotExistException {
        if(shardingColumnNames == null || shardingColumnNames.size() == 0){
            throw new ShardingColumnNotExistException("sharding column not exists , table : " + shardingTableName);
        }
        if(shardingColumnNames.size() > 1){
            throw new BigSqlException(100,"sharding column must be single sharding column");
        }

        Field field = null;
        String shardingColumnName = this.shardingColumnNames.get(0);
        for (Field tmp : fields) {
            if(shardingColumnName.equals(SqlUtil.realName(tmp.getName()))){
                field = tmp;
                break;
            }
        }

        if(field == null){
            throw new BigSqlException(300,"sharding column name [ " + shardingColumnName+ " ] in the sql not found : " + crudStatement.getSql());
        }

        Object o = getSingleValue(field);
        if(TypeUtil.isNumber(o)){
            Long l = Long.parseLong(String.valueOf(o));
            return l.hashCode();
        }else{
            String s = String.valueOf(o).replaceAll("'", "");
            return s.hashCode();
        }
    }


    private Object getSingleValue(Field field){
        if(field instanceof ValueField){
            return ((ValueField) field).getValue();
        }else if(field instanceof ConditionField){
            List<Expression> values = ((ConditionField) field).getValues();
            if(values == null || values.size() != 1){
                throw new BigSqlException(300 , "single column hash sharding must be 1 value");
            }else{
                return values.get(0).getValue();
            }
        }else{
            throw new BigSqlException(300 , "not support field type , " + field.getClass().getName() + " : " + field);
        }
    }
}
