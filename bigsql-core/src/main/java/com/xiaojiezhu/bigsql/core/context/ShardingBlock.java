package com.xiaojiezhu.bigsql.core.context;

import com.xiaojiezhu.bigsql.util.StringUtil;

/**
 * time 2018/8/22 16:45
 *
 * @author xiaojie.zhu <br>
 */
public class ShardingBlock {

    /**
     * the execute dataSource name
     */
    private String dataSourceName;

    private String sql;

    /**
     * sql execute start time
     */
    private long startTime;

    /**
     * 创建statement成功的时间
     */
    private long completeCreateStatementTime;

    /**
     * 完成SQL操作的时间
     */
    private long completeQueryTime;

    private long completeReturnTime;

    /**
     * sql execute over time
     */
    private long endTime;

    private long useTime;


    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public long getStartTime() {
        return startTime;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public long getEndTime() {
        return endTime;
    }

    public void end() {
        if(this.startTime == 0){
            throw new RuntimeException("the start time is null");
        }
        this.endTime = System.currentTimeMillis();
        this.useTime = this.endTime - this.startTime;
    }


    public void completeCreateStatement() {
        this.completeCreateStatementTime = System.currentTimeMillis();
    }

    public void completeQuery() {
        this.completeQueryTime = System.currentTimeMillis();
    }

    public void completeReturn() {
        this.completeReturnTime = System.currentTimeMillis();
    }


    /**
     * 创建statement所需要的时间
     * @return
     */
    public long getCreateStatementUseTime(){
        return this.completeCreateStatementTime - this.startTime;
    }

    public long getQueryUseTime(){
        return this.completeQueryTime - this.completeCreateStatementTime;
    }

    public long getReturnConnectionUseTime(){
        return this.completeReturnTime - this.completeQueryTime;
    }

    public long getLockWaitUseTime(){
        return this.endTime - this.completeReturnTime;
    }


    @Override
    public String toString() {
        return "\t" + dataSourceName + "\t" + StringUtil.removeBlank1(sql) + "\t" + useTime + "("+this.getCreateStatementUseTime()+","+getQueryUseTime()+","+getReturnConnectionUseTime()+","+getLockWaitUseTime()+")";
    }


}
