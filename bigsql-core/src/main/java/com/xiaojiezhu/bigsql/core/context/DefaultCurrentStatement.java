package com.xiaojiezhu.bigsql.core.context;

import com.xiaojiezhu.bigsql.common.LoggerUtil;
import com.xiaojiezhu.bigsql.util.StringUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * time 2018/8/22 16:53
 * bigsql client current execute statement
 * @author xiaojie.zhu <br>
 */
public class DefaultCurrentStatement implements CurrentStatement {
    private int slowQueryTimeOut;


    private String sql;
    private long startTime;
    /**
     * execute sql to every sharding table over time
     */
    private long executeEndTime;


    /**
     * merge sharding table time
     */
    private long mergeCompleteTime;

    private long endTime;

    private List<ShardingBlock> blocks;



    private boolean complete = false;

    public DefaultCurrentStatement(String sql,int slowQueryTimeOut) {
        this.sql = sql;
        this.slowQueryTimeOut = slowQueryTimeOut;
        this.startTime = System.currentTimeMillis();
        this.blocks = new LinkedList<>();
    }

    @Override
    public String getSql() {
        return this.sql;
    }

    @Override
    public void addBlock(ShardingBlock shardingBlock) {
        this.blocks.add(shardingBlock);
    }

    @Override
    public List<ShardingBlock> getBlocks() {
        return this.blocks;
    }

    @Override
    public void executeComplete() {
        this.executeEndTime = System.currentTimeMillis();
    }

    @Override
    public void mergeComplete() {
        if(startTime == 0){
            throw new RuntimeException("startTime is 0 , startTime:" + this.startTime);
        }
        this.mergeCompleteTime = System.currentTimeMillis();

        this.complete();
    }

    @Override
    public void complete() {
        this.complete = true;
        if(startTime == 0 || mergeCompleteTime == 0){
            throw new RuntimeException("startTime or mergeCompleteTime is 0 , startTime:" + this.startTime + " , mergeCompleteTime:" + this.mergeCompleteTime);
        }
        this.endTime = System.currentTimeMillis();
        if(getUseTime() > this.slowQueryTimeOut){
            LoggerUtil.SLOW.warn(this.toString());
        }
    }

    @Override
    public boolean isComplete() {
        return this.complete;
    }

    @Override
    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public long getEndTime() {
        return this.endTime;
    }

    @Override
    public long getUseTime() {
        return this.endTime - this.startTime;
    }

    private long getExecuteUseTime(){
        return this.executeEndTime - this.startTime;
    }

    private long getMergeUseTime(){
        return this.mergeCompleteTime - this.executeEndTime;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (ShardingBlock block : blocks) {
            sb.append(block).append("\n");
        }
        String formatSql = StringUtil.removeBlank1(sql);
        return formatSql + "\t" + this.getUseTime() + "("+this.getExecuteUseTime()+","+this.getMergeUseTime()+")\n" + sb;
    }

    public String toString0() {
        return "DefaultCurrentStatement{" +
                "slowQueryTimeOut=" + slowQueryTimeOut +
                ", sql='" + sql + '\'' +
                ", startTime=" + startTime +
                ", executeEndTime=" + executeEndTime +
                ", mergeCompleteTime=" + mergeCompleteTime +
                ", endTime=" + endTime +
                ", blocks=" + blocks +
                ", complete=" + complete +
                '}';
    }
}
