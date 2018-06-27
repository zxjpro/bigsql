package com.xiaojiezhu.bigsql.sharding.sharding;

import com.xiaojiezhu.bigsql.common.SqlConstant;
import com.xiaojiezhu.bigsql.common.exception.*;
import com.xiaojiezhu.bigsql.sharding.ExecuteBlock;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.SqlUtil;
import com.xiaojiezhu.bigsql.sharding.rule.Rule;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sql.resolve.field.ConditionField;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import com.xiaojiezhu.bigsql.sql.resolve.statement.InsertStatement;
import com.xiaojiezhu.bigsql.sql.resolve.table.ConditionStatement;
import com.xiaojiezhu.bigsql.sql.resolve.table.TableName;
import com.xiaojiezhu.bigsql.util.BeanUtil;
import com.xiaojiezhu.bigsql.util.increment.IncrementFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xiaojie.zhu
 */
public abstract class AbstractShardingStrategy implements ShardingStrategy {
    public static final String DATASOURCE_KEY_NAME = "dataSource";
    public static final String SHARDING_NUMBER_KEY_NAME = "shardingNumber";
    public static final String ALLOW_FULL_SCAN_KEY_NAME = "allowFullScan";
    public static final String INCREMENT_COLUMN_NAME_KEY_NAME = "incrementColumn";

    protected CrudStatement crudStatement;
    /**
     * the logic table name
     */
    protected String shardingTableName;
    protected final ShardingRule shardingRule;
    protected List<String> dataSourceNameList;
    /**
     * auto increment column name list
     */
    protected List<String> incrementColumnList;
    protected int shardingTableNumber;

    /**
     * the config of the strategy sharding column list
     */
    protected List<String> shardingColumnNames = new ArrayList<>(1);
    /**
     * is enable full scan table
     */
    protected boolean allowFullScan = true;

    /**
     * the sql is has sharding column
     */
    protected boolean hasShardingColumn;

    public AbstractShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        this.shardingRule = shardingRule;
        this.crudStatement = crudStatement;
        if(crudStatement == null || crudStatement.getTables() == null || crudStatement.getTables().size() == 0){
            throw new SqlParserException("sql parser fail , it is not crud sql : " + crudStatement.getSql());
        }else{
            TableName tableName = crudStatement.getTables().get(0);
            shardingTableName = tableName.getTableName();
        }

        init();
    }

    /**
     * init the config properties
     */
    private void init(){
        //dataSource List
        Object oDataSourceName = getPropertyValue(DATASOURCE_KEY_NAME , true);
        String dataSourceNames = oDataSourceName.toString();
        String[] split = dataSourceNames.split(",");
        this.dataSourceNameList = Arrays.asList(split);

        //sharding number
        Object oShardingNumber = getPropertyValue(SHARDING_NUMBER_KEY_NAME,true);
        try{
            this.shardingTableNumber = Integer.parseInt(String.valueOf(oShardingNumber));
        }catch (Exception e){
            throw new RuleParserException("parse " + SHARDING_NUMBER_KEY_NAME + " fail , not Integer , " + shardingRule);
        }

        //sharding Column Name
        Object oShardingColumnName = getPropertyValue(Rule.SHARDING_COLUMN_NAME_KEY_NAME,true);
        this.shardingColumnNames = Arrays.asList(oShardingColumnName.toString().split(","));

        //allow full scan,if null ,the default is true
        Object oAllowFullScan = getPropertyValue(ALLOW_FULL_SCAN_KEY_NAME, false);
        if(oAllowFullScan != null){
            try{
                this.allowFullScan = Boolean.parseBoolean(String.valueOf(oAllowFullScan));
            }catch (Exception e){
                //nothing to do
            }
        }


        //increment column
        Object oIncrementColumn = getPropertyValue(INCREMENT_COLUMN_NAME_KEY_NAME, false);
        if(oIncrementColumn != null){
            this.incrementColumnList = Arrays.asList(oIncrementColumn.toString().split(","));
        }
    }


    @Override
    public List<ExecuteBlock> getExecuteBlockList() throws BigSqlException {
        if(crudStatement.getSql().contains(SqlConstant.OR)){
            if(!allowFullScan){
                throw new ShardingColumnNotExistException("sharding column : [ "+ shardingColumnNames +" ] not exits , insert error : " + crudStatement.getSql());
            }
        }
        if(crudStatement instanceof InsertStatement){
            //INSERT
            InsertStatement insertStatement = (InsertStatement) crudStatement;
            List<List<ValueField>> insertValues = insertStatement.getInsertValues();
            //check has sharding column
            checkHasShardingColumn(insertValues.get(0));
            addIncrementColumn(insertStatement, insertValues);
            List<String> insertSql = insertStatement.getInsertSql();
            List<ExecuteBlock> executeBlocks = new ArrayList<>(insertSql.size());
            for (int i = 0 ; i < insertSql.size() ; i ++) {
                String sql = insertSql.get(i);
                ShardingTable shardingTable = getShardingTable(insertValues.get(i));
                sql = SqlUtil.updateTableName(sql,this.shardingTableName,shardingTable.getTableName());
                executeBlocks.add(new ExecuteBlock(shardingTable.getDataSourceName(),sql));
            }
            return executeBlocks;

        }else{
            //SELECT,DELETE,UPDATE
            ConditionStatement conditionStatement = (ConditionStatement) crudStatement;
            List<ConditionField> conditionFields = conditionStatement.getConditionFields();
            //check has sharding column
            checkHasShardingColumn(conditionFields);

            List<ExecuteBlock> executeBlocks = new ArrayList<>(1);
            String sql = crudStatement.getSql();
            if(this.hasShardingColumn){
                //find sharding column
                ShardingTable shardingTable = getShardingTable(conditionFields);
                String shardingSql = SqlUtil.updateTableName(sql,this.shardingTableName,shardingTable.getTableName());
                executeBlocks.add(new ExecuteBlock(shardingTable.getDataSourceName(),shardingSql));
            }else{
                //full scan table
                List<ShardingTable> shardingTables = getShardingTable();
                for (ShardingTable shardingTable : shardingTables) {
                    String shardingSql = SqlUtil.updateTableName(sql,this.shardingTableName,shardingTable.getTableName());
                    executeBlocks.add(new ExecuteBlock(shardingTable.getDataSourceName(),shardingSql));
                }
            }

            return executeBlocks;
        }
    }


    /**
     * check the rule , and add the increment key
     * @param insertValues
     */
    private void addIncrementColumn(InsertStatement insertStatement,List<List<ValueField>> insertValues){
        //检查是否有分布式主键配置
        //如果有，
            //检查SQL中是否有带有这个参数
            //如果有
                //检查所有的插入行中，这个参数必须全部不为NULL，否则要抛出异常
            //如果没有，则需要增加INSERT的列
        if(this.incrementColumnList != null && this.incrementColumnList.size() > 0){
            for (String incrementColumn : this.incrementColumnList) {
                List<ValueField> valueFields = insertValues.get(0);
                int index = getColumnIndex(incrementColumn, valueFields);
                if(index == -1){
                    //sql not has , add increment column value
                    insertStatement.addInsertColumn(incrementColumn,IncrementFactory.nextId(insertValues.size()));
                }else{
                    //sql has value , check
                    for (List<ValueField> insertValue : insertValues) {
                        ValueField valueField = insertValue.get(index);
                        if(BeanUtil.hasEmpty(valueField.getValues())){
                            throw new IncrementColumnNullException(incrementColumn + " has value be null");
                        }
                    }
                }
            }
        }
    }


    /**
     * get the column index
     * @param columnName column name
     * @param valueFields the insert value field
     * @return the index of the insert value field , if not exists , return -1
     */
    private int getColumnIndex(String columnName, List<ValueField> valueFields) {
        for(int i = 0 ; i < valueFields.size() ; i ++){
            ValueField valueField = valueFields.get(i);
            if(columnName.equals(valueField.getName())){
                return i;
            }
        }
        return -1;
    }



    /**
     * check the sql is has sharding column , it not has and not allow full scan table , it will be throw FullScanTableException
     * @param valueFields value field
     */
    private void checkHasShardingColumn(List<? extends ValueField> valueFields){
        if(valueFields != null && valueFields.size() > 0){
            if(this.shardingColumnNames.size() == 1){
                for (ValueField valueField : valueFields) {

                    if(shardingColumnNames.contains(valueField.getName())){
                        if(valueField.getValues() != null && valueField.getValues().size() > 0){
                            this.hasShardingColumn = true;
                            return;
                        }
                    }
                }
            }else{
                //TODO 暂时只支持一个分片键，这里需要增加多个分片键
                throw new RuleParserException("not support multipart sharding column");
            }
        }
        this.hasShardingColumn = false;
        if(!allowFullScan){
            throw new FullScanTableException(crudStatement.getSql() + " not has shardingColumn ");
        }
    }




    /**
     * get the value from the xml properties
     * @param key key
     * @param required required
     * @return value , if not exists,will throw RuleParserException
     */
    private Object getPropertyValue(String key,boolean required){
        Map<String, Object> properties = shardingRule.getProperties();
        Object oValue = properties.get(key);
        if(oValue == null){
            if(required){
                throw new RuleParserException("not found key=" + key + " , rule : " + shardingRule);
            }
        }
        return oValue;
    }



    @Override
    public CrudStatement getStatement() {
        return crudStatement;
    }


    @Override
    public String getName() {
        return getClass().getSimpleName();
    }


    /**
     * get the sharding table by value field or conditionValueField
     * @param valueFields the condition or insert field
     * @return jdbc connection
     */
    protected abstract ShardingTable getShardingTable(List<? extends ValueField> valueFields)throws BigSqlException;

    /**
     * get the all sharding table list<br>
     *
     * should cache , Because it is possible to call many times
     *
     * @return sharding table list
     */
    protected abstract List<ShardingTable> getShardingTable();
}
