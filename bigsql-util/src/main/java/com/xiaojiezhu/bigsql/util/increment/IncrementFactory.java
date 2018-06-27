package com.xiaojiezhu.bigsql.util.increment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class IncrementFactory {
    private static final SnowflakeIdWorker WORKER = new SnowflakeIdWorker(0,0);

    public static long nextId(){
        long id = WORKER.nextId();
        return id;
    }

    public static List<Long> nextId(int len){
        List<Long> ids = new ArrayList<>(len);
        for(int i = 0 ; i < len ; i ++){
            ids.add(nextId());
        }
        return ids;
    }
}
