package com.xiaojiezhu.bigsql.sharding.sharding.hash;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.common.exception.ExternalException;
import com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.rule.sharding.ShardingRule;
import com.xiaojiezhu.bigsql.sharding.sharding.AbstractShardingStrategy;
import com.xiaojiezhu.bigsql.sql.resolve.field.ValueField;
import com.xiaojiezhu.bigsql.sql.resolve.statement.CrudStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    public AbstractHashShardingStrategy(CrudStatement crudStatement, ShardingRule shardingRule) {
        super(crudStatement, shardingRule);
    }


    @Override
    protected ShardingTable getShardingTable(List<? extends ValueField> valueFields)throws BigSqlException {
        //splitNo , the number of sharding table  , index from 1,
        int splitNo = getShardingSplitNo(valueFields);
        String dataSourceName = choseDataSourceName(splitNo);
        return new ShardingTable(this.shardingTableName + "_" + splitNo, dataSourceName);
    }



    @Override
    protected List<ShardingTable> getShardingTable() {
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
                shardingTables.add(new ShardingTable(shardingTableName + "_" + i,dataSourceNameList.get(index)));
            }
        }

        return shardingTables;
    }

    /**
     * create connection list by dataSource
     * @param dataSources dataSource list
     * @return the connection of datasource create
     */
    private List<Connection> createConnection(Set<DataSource> dataSources){
        List<Connection> connections = new ArrayList<>(dataSources.size());
        for (DataSource dataSource : dataSources) {
            Connection connection;
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new ExternalException("dataSource can not create connection" , e);
            }
            connections.add(connection);
        }
        return connections;
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
     * @param valueFields insert value or condition field
     * @return split no , index from 1
     */
    protected int getShardingSplitNo(List<? extends ValueField> valueFields){
        int hashCode = hashShardingValue(valueFields);
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
     * @param valueFields all of insert field or condition field，you should filter you need column field
     * @return hashCode
     * @throws ShardingColumnNotExistException if sharding column not exits ,throw this exception
     */
    protected abstract int hashShardingValue(List<? extends ValueField> valueFields)throws ShardingColumnNotExistException;


}
