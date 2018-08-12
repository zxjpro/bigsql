package com.xiaojiezhu.bigsql.sharding.sharding;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.SqlUtil;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sql.resolve.field.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;

import java.util.List;

/**
 * time 2018/8/10 15:02
 *
 * @author xiaojie.zhu <br>
 */
public abstract class AbstractSingleColumnShardingStrategy extends AbstractShardingStrategy{
    public AbstractSingleColumnShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        super(crudStatement, shardingRule);
    }

    @Override
    protected List<ShardingTable> getExecuteShardingTable(List<? extends Field> fields) throws BigSqlException {
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

        return this.getExecuteShardingTable(field);
    }


    /**
     * get shardingTable by single value filed
     * @param field condition or insert
     * @return
     */
    protected abstract List<ShardingTable> getExecuteShardingTable(Field field);


}
