package com.xiaojiezhu.bigsql.sharding.sharding.hash;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.RuleParserException;
import com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sharding.sharding.AbstractShardingStrategy;
import com.xiaojiezhu.bigsql.sql.resolve.field.Field;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * the abstract of hash sharding strategy , it has single column hash , and multipart column hash
 * @author xiaojie.zhu
 */
public abstract class AbstractHashShardingStrategy extends AbstractShardingStrategy {
    public final static Logger LOG = LoggerFactory.getLogger(AbstractHashShardingStrategy.class);
    /**
     * the logic table all of sharding table
     */
    protected List<ShardingTable> shardingTables;

    protected List<String> dataSourceNameList;

    protected int shardingTableNumber;


    public AbstractHashShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        super(crudStatement, shardingRule);
        this.init();
    }

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

    }


    @Override
    protected List<ShardingTable> getExecuteShardingTable(List<? extends Field> fields)throws BigSqlException {
        //splitNo , the number of sharding table  , index from 1,
        int splitNo = getShardingSplitNo(fields);
        String dataSourceName = choseDataSourceName(splitNo);
        return Collections.singletonList(new ShardingTable(this.logicTableName + "_" + splitNo, dataSourceName));
    }



    @Override
    protected List<ShardingTable> getExecuteShardingTable() {
        if(shardingTables == null){
            shardingTables = new ArrayList<>(shardingTableNumber);
            for(int i = 1 ; i <= shardingTableNumber ; i ++){
                int index;
                int y = shardingTableNumber % dataSourceNameList.size();
                if(y == 0){
                    index = dataSourceNameList.size() - 1;
                }else{
                    index = y - 1;

                }
                shardingTables.add(new ShardingTable(this.logicTableName + "_" + i,dataSourceNameList.get(index)));
            }
        }

        return shardingTables;
    }




    /**
     *
     * chose a dataSource name, by split no
     * @param splitNo  the sharding table no
     * @return
     */
    private String choseDataSourceName(int splitNo)throws ShardingColumnNotExistException{
        int i = splitNo % dataSourceNameList.size();

        int dataSourceIndex;
        if(i == 0){
            dataSourceIndex = dataSourceNameList.size() - 1;
        }else{
            dataSourceIndex = i - 1;
        }
        return this.dataSourceNameList.get(dataSourceIndex);
    }


    /**
     * get the split no of the sharding table , index from 1
     * @param fields insert value or condition field
     * @return split no , index from 1
     */
    protected int getShardingSplitNo(List<? extends Field> fields){
        int hashCode = hashShardingValue(fields);
        int i = hashCode % this.shardingTableNumber;
        int split;
        if(i == 0){
            split = this.shardingTableNumber;
        }else{
            split = i;
        }
        return Math.abs(split);
    }



    /**
     * hash code by sharding value
     * @param fields all of insert field or condition field，you should filter you need column field
     * @return hashCode
     * @throws ShardingColumnNotExistException if sharding column not exits ,throw this exception
     */
    protected abstract int hashShardingValue(List<? extends Field> fields)throws ShardingColumnNotExistException;


}
