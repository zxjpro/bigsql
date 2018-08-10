package com.xiaojiezhu.bigsql.sharding.sharding.time.standard;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.NotSupportException;
import com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sharding.sharding.AbstractShardingStrategy;
import com.xiaojiezhu.bigsql.sharding.sharding.AbstractSingleColumnShardingStrategy;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import com.xiaojiezhu.bigsql.util.BeanUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * time 2018/8/10 9:11
 *
 * @author xiaojie.zhu <br>
 */
public class StandardTimeShardingStrategy extends AbstractSingleColumnShardingStrategy {
    public static final String RANGE = "range";
    public static final String SHARDING_FORMAT = "shardingFormat";

    private Queue<StandardRange> standardRangeQueue;

    public StandardTimeShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        super(crudStatement, shardingRule);
        this.init();
    }

    @Override
    protected List<ShardingTable> getExecuteShardingTable(ValueField valueField) {

        return null;
    }

    private void init(){
        Map<String, Object> properties = shardingRule.getProperties();
        String rangeCmdList = (String) properties.get(RANGE);
        String rangeFormat = (String) properties.get(SHARDING_FORMAT);

        String[] ranges = rangeCmdList.split(";");

        this.standardRangeQueue = new LinkedList<>();

        for (String rangeCmd : ranges) {
            if(!BeanUtil.isStringEmpty(rangeCmd)){
                StandardRange standardRange = new StandardRange(rangeCmd,rangeFormat);
                this.standardRangeQueue.add(standardRange);
            }
        }
    }


    @Override
    protected List<ShardingTable> getExecuteShardingTable() {
        throw new NotSupportException("time sharding not allow full scan table");
    }
}
