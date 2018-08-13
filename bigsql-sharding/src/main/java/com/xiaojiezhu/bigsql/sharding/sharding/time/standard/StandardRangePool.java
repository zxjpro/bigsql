package com.xiaojiezhu.bigsql.sharding.sharding.time.standard;

import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import com.xiaojiezhu.bigsql.sharding.ShardingTable;
import com.xiaojiezhu.bigsql.sql.resolve.field.Expression;
import com.xiaojiezhu.bigsql.util.DateUtils;
import com.xiaojiezhu.bigsql.util.TypeUtil;

import javax.swing.text.InternationalFormatter;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * @author xiaojie.zhu
 */
public class StandardRangePool {

    private String logicTableName;
    private String rangeFormat;
    private List<StandardRange> ranges = new LinkedList<>();

    /**
     * get the table suffix 's date pattern
     */
    private String pattern;

    /**
     * the calendar add field type , calendar.add(,)
     */
    private int incrementType;




    public StandardRangePool(String logicTableName , String rangeFormat) {
        this.logicTableName = logicTableName;
        this.rangeFormat = rangeFormat;

        this.init();
    }

    private void init(){
        if(StandardRange.YEAR.equals(this.rangeFormat)){
            this.pattern = "yyyy";
            this.incrementType = Calendar.YEAR;

        }else if(StandardRange.MONTH.equals(this.rangeFormat)){
            this.pattern = "yyyyMM";
            this.incrementType = Calendar.MONTH;

        }else if(StandardRange.DAY.equals(this.rangeFormat)){
            this.pattern = "yyyyMMdd";
            this.incrementType = Calendar.DAY_OF_MONTH;

        }else{
            throw new BigSqlException(300 , "not support range format : " + this.rangeFormat);
        }
    }




    /**
     * add range
     * @param range
     */
    public void addRange(StandardRange range){
        this.ranges.add(range);
    }

    /**
     * find the date range ,if not found ,return null
     * @param date
     * @return
     */
    public StandardRange findRange(Date date){
        for (StandardRange range : this.ranges) {
            boolean find = range.isRange(date);
            if(find){
                return range;
            }
        }
        return null;
    }

    /**
     * get sharding table list by expressions
     * @param expressions
     * @return
     */
    public List<ShardingTable> getShardingTables(List<Expression> expressions){
        if(expressions == null || expressions.size() != 2){
            throw new BigSqlException(300 , "the standard time sharding condition expression must not be null , and size = 2");
        }
        expressions.sort(EXPRESSION_SORT);

        Expression startExpression = expressions.get(0);
        Expression endExpression = expressions.get(1);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = TypeUtil.parseDate(String.valueOf(startExpression.getValue()));
            endDate = TypeUtil.parseDate(String.valueOf(endExpression.getValue()));
        } catch (ParseException e) {
            throw new BigSqlException(200 , "parse str to date error :" + e.getMessage() , e);
        }

        startDate = this.rangeStartDate(startDate);
        endDate = this.rangeEndDate(endDate);

        List<ShardingTable> shardingTables = new LinkedList<>();


        Date tmp = startDate;
        while (tmp.getTime() < endDate.getTime()){
            shardingTables.add(getShardingTable(tmp));

            tmp = this.incrementDate(tmp);
        }

/*        if(isExcess(endDate)){
            shardingTables.add(getShardingTable(tmp));
        }*/


        return shardingTables;

    }

    private Date rangeEndDate(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        if(StandardRange.YEAR.equals(this.rangeFormat)){
            calendar.set(Calendar.MONTH , 11);
            calendar.set(Calendar.HOUR_OF_DAY , 23);
            calendar.set(Calendar.MINUTE , 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND  ,999);

            int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH , maxDayOfMonth);

        }else if(StandardRange.MONTH.equals(this.rangeFormat)){
            calendar.set(Calendar.HOUR_OF_DAY , 23);
            calendar.set(Calendar.MINUTE , 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND  ,999);

            int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH , maxDayOfMonth);

        }else if(StandardRange.DAY.equals(this.rangeFormat)){
            calendar.set(Calendar.HOUR_OF_DAY , 23);
            calendar.set(Calendar.MINUTE , 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND  ,999);

        }else{
            throw new BigSqlException(300 , "not support rangeFormat : " + this.rangeFormat);
        }

        return calendar.getTime();
    }


    private Date rangeStartDate(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        if(StandardRange.YEAR.equals(this.rangeFormat)){
            calendar.set(Calendar.MONTH , 0);
            calendar.set(Calendar.DAY_OF_MONTH , 1);
            calendar.set(Calendar.HOUR_OF_DAY , 0);
            calendar.set(Calendar.MINUTE , 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND  ,0);

        }else if(StandardRange.MONTH.equals(this.rangeFormat)){
            calendar.set(Calendar.DAY_OF_MONTH , 1);
            calendar.set(Calendar.HOUR_OF_DAY , 0);
            calendar.set(Calendar.MINUTE , 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND  ,0);

        }else if(StandardRange.DAY.equals(this.rangeFormat)){
            calendar.set(Calendar.HOUR_OF_DAY , 0);
            calendar.set(Calendar.MINUTE , 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND  ,0);

        }else{
            throw new BigSqlException(300 , "not support rangeFormat : " + this.rangeFormat);
        }

        return calendar.getTime();
    }

    private boolean isExcess(Date endDate){
        String excess;
        if(StandardRange.YEAR.equals(this.rangeFormat)){
            excess = DateUtils.format(endDate,"MMddHHmmssSSS");
        }else if(StandardRange.MONTH.equals(this.rangeFormat)){
            excess = DateUtils.format(endDate , "ddHHmmssSSS");
        }else if(StandardRange.DAY.equals(this.rangeFormat)){
            excess = DateUtils.format(endDate , "HHmmssSSS");
        }else{
            throw new BigSqlException(300 , "not support rangeFormat : " + this.rangeFormat);
        }

        long l = Long.parseLong(excess);

        return l > 0;
    }



    private Date incrementDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        calendar.add(this.incrementType,1);

        return calendar.getTime();
    }

    /**
     * get the table suffix
     * @param date date
     * @return suffix
     */
    protected String getShardingTableName(Date date){
        String suffix = DateUtils.format(date,this.pattern);
        return this.logicTableName + "_" + suffix;
    }

    /**
     * get date sharding table
     * @param date
     * @return
     */
    public ShardingTable getShardingTable(Date date){
        String shardingTableName = getShardingTableName(date);
        StandardRange range = findRange(date);
        if(range == null){
            throw new NullPointerException("not found standard rage for date : " + date);
        }
        return new ShardingTable(shardingTableName , range.getDataSourceName());

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


}
