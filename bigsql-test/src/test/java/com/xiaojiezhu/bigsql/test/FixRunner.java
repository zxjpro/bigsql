package com.xiaojiezhu.bigsql.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaojie.zhu
 */
public class FixRunner {
    private final Object lock = new Object();
    private int maxCount;
    private AtomicInteger count = new AtomicInteger(0);
    private Executor executor;
    private CountDownLatch cd;


    public FixRunner(int maxCount, Executor executor) {
        this.maxCount = maxCount;
        this.executor = executor;
    }

    public void add(final Runnable runnable){
        if(runnable == null){
            throw new NullPointerException("runnable can not be null");
        }
        if(count.get() >= maxCount){
            cd = new CountDownLatch(1);
            try {
                cd.await();
            } catch (InterruptedException e) {
                throw new RuntimeException("CountDownLatch await error " , e);
            }
        }
        count.incrementAndGet();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                synchronized (lock){
                    if(cd != null){
                        cd.countDown();
                        cd = null;
                    }
                    count.decrementAndGet();
                }
            }
        });
    }


}
