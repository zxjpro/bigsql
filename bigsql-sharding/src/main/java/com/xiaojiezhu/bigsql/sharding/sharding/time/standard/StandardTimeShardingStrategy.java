package com.xiaojiezhu.bigsql.sharding.sharding.time.standard;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.NotSupportException;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sharding.sharding.AbstractSingleColumnShardingStrategy;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.field.Expression;
import com.xiaojiezhu.bigsql.sql.resolve.field.Field;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import com.xiaojiezhu.bigsql.util.DateUtils;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import java.util.*;

/**
 * time 2018/8/10 9:11
 *
 * @author xiaojie.zhu <br>
 */
public class StandardTimeShardingStrategy extends AbstractSingleColumnShardingStrategy {
    public static final String RANGE = "range";
    public static final String SHARDING_FORMAT = "shardingFormat";

    private Queue<StandardRange> standardRangeQueue;

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

        this.standardRangeQueue = new LinkedList<>();

        for (String rangeCmd : ranges) {
            if(!BeanUtil.isStringEmpty(rangeCmd)){
                StandardRange standardRange = new StandardRange(rangeCmd,rangeFormat);
                this.standardRangeQueue.add(standardRange);
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
        if(!TypeUtil.isDate(value)){
            throw new BigSqlException(300 , "standard time sharding value must be date");
        }else{
            Date date = (Date) value;
            StandardRange range = findRange(date);
            if(range == null){
                throw new BigSqlException(300, "not found a date range , tableName:" + shardingTableName + " , date : " + value);
            }else{
                return findShardingTableByRange(date,range);
            }
        }
    }


    /**
     * find the select,delete,update sql sharding tables
     * @param conditionField
     * @return
     */
    protected List<ShardingTable> findShardingTable(ConditionField conditionField){
        List<Expression> expressions = conditionField.getValues();
        if(expressions == null || expressions.size() == 0 || expressions.size() > 2){
            throw new BigSqlException(300 , "the standard time sharding condition expression must not be 2 , and size <= 2");
        }
        expressions.sort(EXPRESSION_SORT);

        Expression startExpression = expressions.get(0);
        Expression endExpression = expressions.get(1);

        return null;
    }


    /**
     * get the single value sharding table ,this is a insert sql
     * @param date
     * @param range
     * @return
     */
    protected List<ShardingTable> findShardingTableByRange(Date date,StandardRange range){
        String tableName = this.getTableName(date);
        ShardingTable shardingTable = new ShardingTable(tableName,range.getDataSourceName());
        return Collections.singletonList(shardingTable);
    }


    /**
     * get the table name
     * @param date date
     * @return tableName
     */
    protected final String getTableName(Date date){
        String suffix = getSuffix(this.rangeFormat, date);

        return this.shardingTableName + "_" + suffix;
    }


    /**
     * get the table suffix
     * @param rangeFormat format
     * @param date date
     * @return suffix
     */
    private String getSuffix(String rangeFormat,Date date){
        String suffix;
        if(StandardRange.YEAR.equals(rangeFormat)){
            suffix = DateUtils.format(date, "yyyy");

        }else if(StandardRange.MONTH.equals(rangeFormat)){
            suffix = DateUtils.format(date,"yyyyMM");

        }else if(StandardRange.DAY.equals(rangeFormat)){
            suffix = DateUtils.format(date,"yyyyMMdd");

        }else{
            throw new BigSqlException(300 , "not support range format : " + rangeFormat);
        }

        return suffix;
    }





    /**
     * find the date range ,if not found ,return null
     * @param date
     * @return
     */
    protected StandardRange findRange(Date date){
        StandardRange range;
        while ((range = this.standardRangeQueue.poll()) != null){
            boolean find = range.isRange(date);
            if(find){
                return range;
            }
        }
        return null;
    }


    /**
     * default expression sorter
     */
    private static Comparator<Expression> EXPRESSION_SORT = new Comparator<Expression>() {
        @Override
        public int compare(Expression o1, Expression o2) {
            if(TypeUtil.isDate(o1.getValue()) && TypeUtil.isDate(o2.getValue())){
                Date d1 = (Date) o1.getValue();
                Date d2 = (Date) o2.getValue();
                return d1.compareTo(d2);

            }else{
                return 1;
            }
        }
    };


    /**
     * get the all of sharding table , it is too big , so time sharding not allow to do this
     * @return
     */
    @Override
    protected List<ShardingTable> getExecuteShardingTable() {
        throw new NotSupportException("time sharding not allow full scan table");
    }
}
