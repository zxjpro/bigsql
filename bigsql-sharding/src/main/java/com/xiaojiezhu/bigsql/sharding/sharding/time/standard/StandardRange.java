package com.xiaojiezhu.bigsql.sharding.sharding.time.standard;

import com.xiaojiezhu.bigsql.common.exception.RuleParserException;
import com.xiaojiezhu.bigsql.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * time 2018/8/10 11:56
 *
 * @author xiaojie.zhu <br>
 */
public class StandardRange {
    public static final String YEAR = "YEAR";
    public static final String MONTH = "MONTH";
    public static final String DAY = "DAY";

    public static final String FLAG = "?";
    public static final String RANGE_DATE_FORMAT = "yyyyMMdd";


    private String rangeCmd;

    /**
     * YEAR,MONTH,DAY..
     */
    private String rangeFormat;

    private String dataSourceName;

    private String startTime;

    private String endTime;

    /**
     * [yyyyMMdd-yyyyMMdd|?]=dataSourceName
     * @param rangeCmd
     */
    public StandardRange(String rangeCmd,String rangeFormat) {
        this.rangeCmd = rangeCmd.trim();
        this.rangeFormat = rangeFormat;

        this.init();
    }


    private void init(){
        //[20170101-20180101]=dataSource1
        String[] s1 = rangeCmd.split("=");
        this.dataSourceName = s1[1];

        String range = s1[0].substring(1,s1[0].length() - 1);

        String[] s2 = range.split("-");

        this.startTime = s2[0];
        this.endTime = s2[1];
    }


    /**
     * the date is on the range
     * @param date
     * @return
     */
    public boolean isRange(Date date){
        if(FLAG.equals(endTime)){
            return true;
        }else{
            Date startDate = DateUtils.parse(startTime, RANGE_DATE_FORMAT);
            Date endDate = DateUtils.parse(endTime, RANGE_DATE_FORMAT);
            if((endDate.getTime() - startDate.getTime()) <= 0){
                throw new RuleParserException("end time must greet start time : " + rangeCmd);
            }

            Calendar startCa = Calendar.getInstance();
            startCa.setTime(startDate);
            int startMonth = startCa.get(Calendar.MONTH) + 1;
            int startDay = startCa.get(Calendar.DAY_OF_MONTH);

            Calendar endCa = Calendar.getInstance();
            endCa.setTime(endDate);
            int endMonth = endCa.get(Calendar.MONTH) + 1;
            int endDay = endCa.get(Calendar.DAY_OF_MONTH);

            if(YEAR.equals(this.rangeFormat)){
                //range by year
                if(startMonth != 1 || startDay != 1 || endMonth != 1 || endDay != 1){
                    throw new RuleParserException("if you sharding by standard year , start time and end time must be month=01 , day=01 : " + rangeCmd);
                }
            }else if(MONTH.equals(this.rangeFormat)){
                //range by month
                if(startDay != 1 || endDay != 1){
                    throw new RuleParserException("if you sharding by standard year , start time and end time must be day=01 : " + rangeCmd);
                }
            }else if(DAY.equals(this.rangeFormat)){
                //nothing to do
            }else{
                throw new RuleParserException("not support range format : " + this.rangeFormat);
            }

            if(date.getTime() >= startDate.getTime() && date.getTime() < endDate.getTime()){
                return true;
            }else{
                return false;
            }


        }
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

}
