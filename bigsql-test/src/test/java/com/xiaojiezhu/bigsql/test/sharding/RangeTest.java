package com.xiaojiezhu.bigsql.test.sharding;

import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sharding.sharding.time.standard.StandardRange;
import com.xiaojiezhu.bigsql.sharding.sharding.time.standard.StandardRangePool;
import com.xiaojiezhu.bigsql.sql.resolve.field.Expression;
import com.xiaojiezhu.bigsql.util.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * time 2018/8/13 9:02
 *
 * @author xiaojie.zhu <br>
 */
public class RangeTest {

    @Test
    public void testYear(){
        StandardRangePool rangePool = new StandardRangePool("person" , "YEAR");

        rangePool.addRange(new StandardRange("[20120101-20190101]=idc49","YEAR"));

        List<Expression> expressions = new LinkedList<>();
        expressions.add(new Expression(">=", "2012-01-01 11:02:01"));
        expressions.add(new Expression("<=","2018-03-01 11:02:01"));

        List<ShardingTable> shardingTables = rangePool.getShardingTables(expressions);

        System.out.println(shardingTables);

        Assert.assertEquals("person_2012 : idc49",shardingTables.get(0).toString());
        Assert.assertEquals("person_2013 : idc49",shardingTables.get(1).toString());
        Assert.assertEquals("person_2014 : idc49",shardingTables.get(2).toString());
        Assert.assertEquals("person_2015 : idc49",shardingTables.get(3).toString());
        Assert.assertEquals("person_2016 : idc49",shardingTables.get(4).toString());
        Assert.assertEquals("person_2017 : idc49",shardingTables.get(5).toString());
        Assert.assertEquals("person_2018 : idc49",shardingTables.get(6).toString());

        Assert.assertEquals(7 , shardingTables.size());
    }

    @Test
    public void testMonth(){
        StandardRangePool rangePool = new StandardRangePool("person" , "MONTH");

        rangePool.addRange(new StandardRange("[20160101-20170101]=idc49","MONTH"));
        rangePool.addRange(new StandardRange("[20170101-20180101]=idc48","MONTH"));

        List<Expression> expressions = new LinkedList<>();
        expressions.add(new Expression(">=", "2016-05-01 11:02:01"));
        expressions.add(new Expression("<=","2017-03-09 11:02:01"));

        List<ShardingTable> shardingTables = rangePool.getShardingTables(expressions);

        System.out.println(shardingTables);

        Assert.assertEquals("person_201605 : idc49",shardingTables.get(0).toString());
        Assert.assertEquals("person_201606 : idc49",shardingTables.get(1).toString());
        Assert.assertEquals("person_201607 : idc49",shardingTables.get(2).toString());
        Assert.assertEquals("person_201608 : idc49",shardingTables.get(3).toString());
        Assert.assertEquals("person_201609 : idc49",shardingTables.get(4).toString());
        Assert.assertEquals("person_201610 : idc49",shardingTables.get(5).toString());
        Assert.assertEquals("person_201611 : idc49",shardingTables.get(6).toString());
        Assert.assertEquals("person_201612 : idc49",shardingTables.get(7).toString());
        Assert.assertEquals("person_201701 : idc48",shardingTables.get(8).toString());
        Assert.assertEquals("person_201702 : idc48",shardingTables.get(9).toString());
        Assert.assertEquals("person_201703 : idc48",shardingTables.get(10).toString());

        Assert.assertEquals(11 , shardingTables.size());
    }

    @Test
    public void testDay(){
        StandardRangePool rangePool = new StandardRangePool("person" , "DAY");

        rangePool.addRange(new StandardRange("[20180101-20190101]=idc49","DAY"));

        List<Expression> expressions = new LinkedList<>();
        expressions.add(new Expression(">=", "2018-02-026 11:02:01"));
        expressions.add(new Expression("<=","2018-03-02"));

        List<ShardingTable> shardingTables = rangePool.getShardingTables(expressions);

        System.out.println(shardingTables);

        Assert.assertEquals("person_20180226 : idc49",shardingTables.get(0).toString());
        Assert.assertEquals("person_20180227 : idc49",shardingTables.get(1).toString());
        Assert.assertEquals("person_20180228 : idc49",shardingTables.get(2).toString());
        Assert.assertEquals("person_20180301 : idc49",shardingTables.get(3).toString());
        Assert.assertEquals("person_20180302 : idc49",shardingTables.get(4).toString());


        Assert.assertEquals(5 , shardingTables.size());
    }




}
