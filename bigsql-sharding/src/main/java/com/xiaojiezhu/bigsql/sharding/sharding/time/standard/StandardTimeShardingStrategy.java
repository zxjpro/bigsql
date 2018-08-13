package com.xiaojiezhu.bigsql.sharding.sharding.time.standard;

import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.NotSupportException;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.SqlUtil;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sharding.sharding.AbstractSingleColumnShardingStrategy;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.field.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * time 2018/8/10 9:11
 *
 * @author xiaojie.zhu <br>
 */
public class StandardTimeShardingStrategy extends AbstractSingleColumnShardingStrategy {
    public static final String RANGE = "range";
    public static final String SHARDING_FORMAT = "shardingFormat";
    public static final int MAX_QUERY_SHARDING_SIZE = 12;

    private StandardRangePool standardRangePool;

    /**
     * YEAR,MONTH,DAY
     */
    protected String rangeFormat;

    public StandardTimeShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        super(crudStatement, shardingRule);
        this.init();
    }


    private void init(){
        Map<String, Object> properties = shardingRule.getProperties();
        String rangeCmdList = (String) properties.get(RANGE);
        this.rangeFormat = (String) properties.get(SHARDING_FORMAT);

        String[] ranges = rangeCmdList.split(";");

        this.standardRangePool = new StandardRangePool(this.logicTableName,this.rangeFormat);

        for (String rangeCmd : ranges) {
            if(!BeanUtil.isStringEmpty(rangeCmd)){
                StandardRange standardRange = new StandardRange(rangeCmd,rangeFormat);
                this.standardRangePool.addRange(standardRange);
            }
        }
    }

    @Override
    protected List<ShardingTable> getExecuteShardingTable(Field field) {
        if(field instanceof ValueField){
            //insert
            return findShardingTable((ValueField)field);

        }else if(field instanceof ConditionField){
            //select,update,delete
            return findShardingTable((ConditionField) field);

        }else{
            throw new BigSqlException("not support field type on standard time sharding : " + field.getClass().getName());
        }
    }

    /**
     * find insert sql sharding table
     * @param valueField
     * @return
     */
    protected List<ShardingTable> findShardingTable(ValueField valueField){
        Object value = valueField.getValue();
        if(SqlConstant.NOW.equalsIgnoreCase(String.valueOf(value))){
            value = new Date();
        }
        if(!TypeUtil.isDate(value)){
            try {
                value = TypeUtil.parseDate(String.valueOf(value).replaceAll("'",""));
            } catch (ParseException e) {
                throw new BigSqlException(200 , value + " can not parse date ");
            }
        }

        Date date = (Date) value;
        ShardingTable shardingTable = this.standardRangePool.getShardingTable(date);
        if(shardingTable == null){
            throw new BigSqlException(300, "not found a shardingTable , tableName:" + this.logicTableName + " , date : " + value);
        }else{
            return Collections.singletonList(shardingTable);
        }
    }


    /**
     * find the select,delete,update sql sharding tables
     * @param conditionField
     * @return
     */
    protected List<ShardingTable> findShardingTable(ConditionField conditionField){
        List<ShardingTable> shardingTables = this.standardRangePool.getShardingTables(conditionField.getValues());
        if(shardingTables.size() > MAX_QUERY_SHARDING_SIZE){
            throw new BigSqlException(300 , "time sharding must not query more than " + MAX_QUERY_SHARDING_SIZE + " sharding table size , this is : " + shardingTables.size());
        }
        return shardingTables;
    }



    /**
     * get the all of sharding table , it is too big , so time sharding not allow to do this
     * @return
     */
    @Override
    protected List<ShardingTable> getExecuteShardingTable() {
        throw new NotSupportException("time sharding not allow full scan table");
    }
}
