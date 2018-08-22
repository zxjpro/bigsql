package com.xiaojiezhu.bigsql.core.context;

import java.util.List;

/**
 * time 2018/8/22 16:44
 *
 * the bigsql client current execute statement
 * @author xiaojie.zhu <br>
 */
public interface CurrentStatement {

    /**
     * get the current execute sql
     * @return
     */
    String getSql();

    /**
     * add block
     * @param shardingBlock
     */
    void addBlock(ShardingBlock shardingBlock);

    /**
     * get all execute blocks
     * @return
     */
    List<ShardingBlock> getBlocks();

    /**
     * execute statement complete current sql , but not merge
     */
    void executeComplete();

    /**
     * merge sharding table complete
     */
    void mergeComplete();


    /**
     * complete this sql
     */
    void complete();

    /**
     * the statement is execute complete
     * @return
     */
    boolean isComplete();

    /**
     * get the statement start execute time
     * @return
     */
    long getStartTime();

    /**
     * get the statement execute end time
     * @return
     */
    long getEndTime();

    /**
     * get the statement execute use time
     * @return
     */
    long getUseTime();


}
