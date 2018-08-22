package com.xiaojiezhu.bigsql.core.context;

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

    @Override
    public String toString() {
        return "\t" + dataSourceName + "\t" + sql.replaceAll("\\\n"," ") + "\t" + useTime;
    }
}
